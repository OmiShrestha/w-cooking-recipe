package com.miomi.recipe

import com.miomi.recipe.data.repository.RecipeRepository
import com.miomi.recipe.model.Ingredient
import com.miomi.recipe.model.Recipe
import com.miomi.recipe.model.RecipeWithDetails
import com.miomi.recipe.model.Step
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeRecipeRepository : RecipeRepository {

    companion object {
        val categories = listOf("Breakfast", "Lunch", "Dinner", "Dessert")
    }

    val recipes = mutableListOf<Recipe>()
    val ingredients = mutableListOf<Ingredient>()
    val steps = mutableListOf<Step>()

    private val recipesFlow = MutableStateFlow<List<Recipe>>(emptyList())
    private val favoritesFlow = MutableStateFlow<List<Recipe>>(emptyList())

    fun seedRecipes(vararg seed: Recipe) {
        recipes.addAll(seed)
        recipesFlow.value = recipes.toList()
        favoritesFlow.value = recipes.filter { it.isFavorite }
    }

    override fun getRecipesStream(): Flow<List<Recipe>> {
        return recipesFlow
    }

    override fun getFavoritesStream(): Flow<List<Recipe>> {
        return favoritesFlow
    }

    override fun getRecipeWithDetails(recipeId: Int): Flow<RecipeWithDetails> {
        val recipe = recipes.first { it.recipeId == recipeId }
        val recipeIngredients = ingredients.filter { it.recipeId == recipeId }
        val recipeSteps = steps.filter { it.recipeId == recipeId }
        return flowOf(RecipeWithDetails(recipe, recipeIngredients, recipeSteps))
    }

    override suspend fun insertRecipe(recipe: Recipe): Long {
        val id = (recipes.size + 1).toLong()
        recipes.add(recipe.copy(recipeId = id.toInt()))
        recipesFlow.value = recipes.toList()
        return id
    }

    override suspend fun insertStep(step: Step): Long {
        steps.add(step)
        return steps.size.toLong()
    }

    override suspend fun insertIngredient(ingredient: Ingredient): Long {
        ingredients.add(ingredient)
        return ingredients.size.toLong()
    }

    override suspend fun updateIsFavorite(recipeId: Int, isFavorite: Boolean) {
        val index = recipes.indexOfFirst { it.recipeId == recipeId }
        if (index != -1) {
            recipes[index] = recipes[index].copy(isFavorite = isFavorite)
            recipesFlow.value = recipes.toList()
            favoritesFlow.value = recipes.filter { it.isFavorite }
        }
    }

    override suspend fun updateRecipe(recipe: Recipe) {
        val index = recipes.indexOfFirst { it.recipeId == recipe.recipeId }
        if (index != -1) {
            recipes[index] = recipe
            recipesFlow.value = recipes.toList()
        }
    }

    override suspend fun deleteAllIngredientsForRecipe(recipeId: Int) {
        ingredients.removeAll { it.recipeId == recipeId }
    }

    override suspend fun deleteAllStepsForRecipe(recipeId: Int) {
        steps.removeAll { it.recipeId == recipeId }
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        recipes.removeAll { it.recipeId == recipe.recipeId }
        ingredients.removeAll { it.recipeId == recipe.recipeId }
        steps.removeAll { it.recipeId == recipe.recipeId }
        recipesFlow.value = recipes.toList()
        favoritesFlow.value = recipes.filter { it.isFavorite }
    }

}