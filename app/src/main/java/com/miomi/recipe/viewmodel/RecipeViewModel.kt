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

    fun getCategories(): List<String> {
        return RecipeRepository.categories
    }

    //TODO: this is not how recipes should be created. I am only doing this to get the app compiling
    fun addRecipe(name: String, category: String, ingredients: String, instructions: String) {
        viewModelScope.launch {
            val newId = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
            val recipe = Recipe(newId, name, category, true)
            val ingredient = Ingredient(newId, ingredients, 3, "sample unit")
            val step = Step(newId, 0, instructions)

            recipeRepository.insertRecipe(recipe)
            recipeRepository.insertIngredient(ingredient)
            recipeRepository.insertStep(step)
        }
    }

    //One shot retrieval, used for detail screen navigation
    fun getRecipeById(id: Int): Recipe? {
        return recipeRepository.getRecipeById(id)
    }

}