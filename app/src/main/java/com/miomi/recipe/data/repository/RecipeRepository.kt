package com.miomi.recipe.data.repository

import com.miomi.recipe.model.Ingredient
import com.miomi.recipe.model.Recipe
import com.miomi.recipe.model.RecipeWithDetails
import com.miomi.recipe.model.Step
import kotlinx.coroutines.flow.Flow



interface RecipeRepository {

    companion object {
        val categories = listOf("Breakfast", "Lunch", "Dinner", "Dessert")
    }

    // returns a stream of all recipes, used to populate the recipe list screen
    fun getRecipesStream(): Flow<List<Recipe>>

    // returns a stream of recipes marked as favorites, used to populate the favorites screen
    fun getFavoritesStream(): Flow<List<Recipe>>

    // this method returns a RecipeWithDetails object that includes the ingredients and steps lists
    // it is used in the recipe details section
    fun getRecipeWithDetails(recipeId: Int): Flow<RecipeWithDetails>

    suspend fun insertRecipe(recipe: Recipe): Long

    suspend fun insertStep(step: Step): Long

    suspend fun insertIngredient(ingredient: Ingredient): Long

    suspend fun updateIsFavorite(recipeId: Int, isFavorite: Boolean)
}