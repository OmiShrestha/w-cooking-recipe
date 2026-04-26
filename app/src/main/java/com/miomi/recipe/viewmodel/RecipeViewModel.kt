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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class RecipeViewModel(private val  recipeRepository: RecipeRepository ) : ViewModel()
{

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = // 4
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                            as RecipeApplication).recipeRepository
                RecipeViewModel(
                    recipeRepository = myRepository
                )
            }
        }
    }

    val recipesFlow = recipeRepository.getAllRecipesStream()
    val favoritesFlow = recipeRepository.getAllFavoritesStream()

    fun toggleFavorite(recipeId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            recipeRepository.updateIsFavorite(recipeId, !isFavorite)
        }
    }

    fun getCategories(): List<String> {
        return RecipeRepository.categories
    }

    //TODO: this is not how recipes should be created. I am only doing this to get the app compiling
    fun getRecipeStream(id: Int): Flow<Recipe?> = recipeRepository.getRecipeById(id)
    fun getIngredientsStream(recipeId: Int): Flow<List<Ingredient>> = recipeRepository.getIngredientsStream(recipeId)
    fun getStepsStream(recipeId: Int): Flow<List<Step>> = recipeRepository.getStepsStream(recipeId)

    fun newAddRecipe(formState: AddRecipeFormState){
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
                        quantity = entry.quantity.toIntOrNull() ?: 0,
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

    fun addRecipe(name: String, category: String, ingredients: String, instructions: String) {
        viewModelScope.launch {
            val newId = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
            recipeRepository.insertRecipe(Recipe(newId, name, category, false))
            recipeRepository.insertIngredient(Ingredient(recipeId = newId, name = ingredients, quantity = 1, unit = ""))
            recipeRepository.insertStep(Step(recipeId = newId, sequenceNum = 1, step = instructions))
        }
    }

}