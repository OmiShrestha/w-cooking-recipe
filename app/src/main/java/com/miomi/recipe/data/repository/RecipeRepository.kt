package com.miomi.recipe.data.repository

import com.miomi.recipe.model.Ingredient
import com.miomi.recipe.model.Recipe
import com.miomi.recipe.model.Step
import kotlinx.coroutines.flow.Flow



interface RecipeRepository {

    //TODO: Consider adding these to their own table
    companion object {
        val categories = listOf("Breakfast", "Lunch", "Dinner", "Dessert")
    }

    //TODO: I'd like to get this working again for the new recipes
    //Test data
//    private fun loadTestData() {
//        // Breakfast
//        addRecipe( "Scrambled Eggs", "Breakfast",  "Eggs, Butter, Salt, Pepper", "Whisk eggs, melt butter in pan, cook on low heat")
//        addRecipe( "Pancakes", "Breakfast",  "Flour, Milk, Eggs, Baking Powder, Sugar", "Mix dry and wet ingredients, cook on griddle")
//        addRecipe( "Avocado Toast", "Breakfast",  "Bread, Avocado, Lemon, Salt, Red Pepper Flakes", "Toast bread, mash avocado, season and spread")
//
//        // Lunch
//        addRecipe( "Grilled Cheese", "Lunch",  "Bread, Cheese, Butter", "Butter bread, add cheese, grill until golden")
//        addRecipe( "Caesar Salad", "Lunch",  "Romaine, Croutons, Parmesan, Caesar Dressing", "Toss romaine with dressing, top with croutons")
//        addRecipe( "Tomato Soup", "Lunch",  "Tomatoes, Onion, Garlic, Cream, Broth", "Saute onion and garlic, add tomatoes, blend smooth")
//
//        //Dinner
//        addRecipe("Pasta Marinara", "Dinner",  "Pasta, Marinara Sauce, Garlic, Olive Oil", "Boil pasta, saute garlic, toss with sauce")
//        addRecipe("Grilled Chicken", "Dinner",  "Chicken Breast, Olive Oil, Garlic, Spices", "Marinate chicken, grill 6 minutes per side")
//        addRecipe("Beef Tacos", "Dinner",  "Ground Beef, Taco Shells, Cheese, Lettuce, Salsa", "Brown beef with seasoning, assemble tacos")
//
//        // Dessert
//        addRecipe( "Brownies", "Dessert",  "Chocolate, Butter, Sugar, Eggs, Flour", "Melt chocolate and butter, mix in remaining ingredients, bake")
//        addRecipe( "Chocolate Chip Cookies", "Dessert",  "Flour, Butter, Sugar, Eggs, Chocolate Chips", "Cream butter and sugar, mix in remaining, bake at 375")
//        addRecipe("Vanilla Ice Cream", "Dessert",  "Heavy Cream, Milk, Sugar, Vanilla Extract, Egg Yolks", "Mix ingredients, churn in ice cream maker, freeze")
//
//    }

    fun getAllRecipesStream(): Flow<List<Recipe>>

    fun getRecipeById(recipeId: Int): Recipe?
    fun getAllFavoritesStream(): Flow<List<Recipe>>
    fun getStepsStream(recipeId: Int): Flow<List<Step>>
    fun getIngredientsStream(recipeId: Int): Flow<List<Ingredient>>

    suspend fun insertRecipe(recipe: Recipe)
    suspend fun insertStep(step: Step)
    suspend fun insertIngredient(ingredient: Ingredient)
    suspend fun updateIsFavorite(recipeId: Int, isFavorite: Boolean)
}