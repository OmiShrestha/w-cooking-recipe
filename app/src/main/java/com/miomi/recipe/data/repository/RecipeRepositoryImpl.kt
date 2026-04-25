package com.miomi.recipe.data.repository

import com.miomi.recipe.data.RecipeDatabase
import com.miomi.recipe.model.Ingredient
import com.miomi.recipe.model.Recipe
import com.miomi.recipe.model.Step
import kotlinx.coroutines.flow.Flow

class RecipeRepositoryImpl (private val recipeDb: RecipeDatabase) : RecipeRepository{
    override fun getAllRecipesStream(): Flow<List<Recipe>> = recipeDb.recipeDao().getAllRecipes()

    override fun getRecipeById(recipeId: Int): Recipe = recipeDb.recipeDao().getRecipeById(recipeId)

    override fun getAllFavoritesStream(): Flow<List<Recipe>> = recipeDb.recipeDao().getFavoriteRecipes()

    override fun getStepsStream(recipeId: Int): Flow<List<Step>> = recipeDb.stepDao().getStepsForRecipe(recipeId)

    override fun getIngredientsStream(recipeId: Int): Flow<List<Ingredient>> = recipeDb.ingredientDao().getAllIngredientsForRecipe(recipeId)

    override suspend fun insertRecipe(recipe: Recipe) = recipeDb.recipeDao().insertRecipe(recipe)

    override suspend fun insertStep(step: Step) = recipeDb.stepDao().insertStep(step)

    override suspend fun insertIngredient(ingredient: Ingredient) = recipeDb.ingredientDao().insertIngredient(ingredient)

    override suspend fun updateIsFavorite(recipeId: Int, isFavorite: Boolean) = recipeDb.recipeDao().updateIsFavorite(recipeId, isFavorite)


}