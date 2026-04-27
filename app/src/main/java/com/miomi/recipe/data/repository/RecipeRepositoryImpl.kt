package com.miomi.recipe.data.repository

import com.miomi.recipe.data.RecipeDatabase
import com.miomi.recipe.model.Ingredient
import com.miomi.recipe.model.Recipe
import com.miomi.recipe.model.RecipeWithDetails
import com.miomi.recipe.model.Step
import kotlinx.coroutines.flow.Flow

class RecipeRepositoryImpl (private val recipeDb: RecipeDatabase) : RecipeRepository{
    override fun getRecipesStream(): Flow<List<Recipe>> = recipeDb.recipeDao().getAllRecipes()

    override fun getFavoritesStream(): Flow<List<Recipe>> = recipeDb.recipeDao().getFavoriteRecipes()

    override fun getRecipeWithDetails(recipeId: Int): Flow<RecipeWithDetails> = recipeDb.recipeDao().getRecipeWithDetails(recipeId)

    override suspend fun insertRecipe(recipe: Recipe): Long = recipeDb.recipeDao().insertRecipe(recipe)

    override suspend fun insertStep(step: Step): Long = recipeDb.stepDao().insertStep(step)

    override suspend fun insertIngredient(ingredient: Ingredient): Long = recipeDb.ingredientDao().insertIngredient(ingredient)

    override suspend fun updateIsFavorite(recipeId: Int, isFavorite: Boolean) = recipeDb.recipeDao().updateIsFavorite(recipeId, isFavorite)

}