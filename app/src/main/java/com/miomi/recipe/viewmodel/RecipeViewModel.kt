package com.miomi.recipe.viewmodel

import androidx.lifecycle.ViewModel
import com.miomi.recipe.data.RecipeRepository
import com.miomi.recipe.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecipeViewModel : ViewModel() {
    private val recipeRepo = RecipeRepository()

    private val _recipes = MutableStateFlow(recipeRepo.getAllRecipes())

    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    fun getCategories(): List<String> {
        return RecipeRepository.categories
    }

    fun addRecipe(name: String, category: String, ingredients: String, instructions: String) {
        recipeRepo.addRecipe(name, category, ingredients, instructions)
        _recipes.value = recipeRepo.getAllRecipes()
    }

    //One shot retrieval, used for detail screen navigation
    fun getRecipeById(id: Int): Recipe? {
        return recipeRepo.getRecipeById(id)
    }

    //Not currently used
    fun getAllRecipes(): List<Recipe> {
        return recipeRepo.getAllRecipes()
    }

    //Not currently used
    fun getRecipesByCategory(category: String): List<Recipe> {
        return recipeRepo.getRecipesByCategory(category)
    }
}