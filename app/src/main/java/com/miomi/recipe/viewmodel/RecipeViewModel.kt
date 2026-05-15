package com.miomi.recipe.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.miomi.recipe.RecipeApplication
import com.miomi.recipe.data.repository.RecipeRepository
import com.miomi.recipe.model.Ingredient
import com.miomi.recipe.model.Recipe
import com.miomi.recipe.model.Step
import kotlinx.coroutines.launch

/**
 * Main ViewModel for the app
 * Provides data streams for the UI and handles all database operations.
 * Scoped to the main navGraph so that it can be shared across all screens
 * */
class RecipeViewModel(private val  recipeRepository: RecipeRepository ) : ViewModel()
{

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                            as RecipeApplication).recipeRepository
                RecipeViewModel(
                    recipeRepository = myRepository
                )
            }
        }
    }

    val recipesFlow = recipeRepository.getRecipesStream()
    val favoritesFlow = recipeRepository.getFavoritesStream()

    fun toggleFavorite(recipeId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            recipeRepository.updateIsFavorite(recipeId, !isFavorite)
        }
    }

    fun getCategories(): List<String> {
        return RecipeRepository.categories
    }

    fun getRecipeWithDetails(recipeId: Int) = recipeRepository.getRecipeWithDetails(recipeId)

    fun addRecipe(formState: AddRecipeFormState){
        viewModelScope.launch {
            val recipeId = recipeRepository.insertRecipe(
                Recipe(
                    name = formState.recipeName,
                    category = formState.category,
                    isFavorite = false
                )
            )
            formState.ingredients.forEach { entry ->
                recipeRepository.insertIngredient(
                    Ingredient(
                        recipeId = recipeId.toInt(),
                        name = entry.name,
                        quantity = entry.quantity.toDoubleOrNull() ?: 0.0,
                        unit = entry.unit
                    )
                )
            }

            formState.steps.forEachIndexed { index, entry ->
                recipeRepository.insertStep(
                    Step(
                        recipeId = recipeId.toInt(),
                        sequenceNum = index + 1,
                        step = entry.description
                    )
                )
            }
        }
    }

    fun deleteRecipe(recipe: Recipe, onDeleted: () -> Unit) {
        viewModelScope.launch {
            recipeRepository.deleteRecipe(recipe)
            onDeleted()
        }
    }

    // updates an existing recipe by first updating the recipe table, then deleting/reinserting all associated ingredients and steps
    fun updateRecipe(formState: AddRecipeFormState) {
        val recipeId = formState.recipeId ?: return
        viewModelScope.launch {
            recipeRepository.updateRecipe(
                Recipe(
                    recipeId = recipeId,
                    name = formState.recipeName,
                    category = formState.category,
                    isFavorite = formState.isFavorite
                )
            )
            recipeRepository.deleteAllIngredientsForRecipe(recipeId)
            formState.ingredients.forEach { entry ->
                recipeRepository.insertIngredient(
                    Ingredient(
                        recipeId = recipeId,
                        name = entry.name,
                        quantity = entry.quantity.toDoubleOrNull() ?: 0.0,
                        unit = entry.unit
                    )
                )
            }
            recipeRepository.deleteAllStepsForRecipe(recipeId)
            formState.steps.forEachIndexed { index, entry ->
                recipeRepository.insertStep(
                    Step(
                        recipeId = recipeId,
                        sequenceNum = index + 1,
                        step = entry.description
                    )
                )
            }
        }
    }
    
}