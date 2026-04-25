package com.miomi.recipe.data

import com.miomi.recipe.model.Recipe
import kotlin.collections.filter
import kotlin.collections.find
import kotlin.collections.plus
import kotlin.collections.sortedWith
import kotlin.text.equals
import kotlin.text.lowercase

class RecipeRepository {

    companion object {
        val categories = listOf("Breakfast", "Lunch", "Dinner", "Dessert")
    }
    private var recipeList: List<Recipe> = emptyList()

    //Uncomment to load test data
//    init {
//        loadTestData()
//    }

    fun addRecipe(name: String, category: String, ingredients: String, instructions: String) {
        val newId = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
        recipeList = recipeList + Recipe(newId, name, category, ingredients, instructions)
    }

    fun getAllRecipes(): List<Recipe> {
        return recipeList.sortedWith(compareBy { it.name.lowercase() })
    }

    fun getRecipeById(id: Int): Recipe? {
        return recipeList.find { it.id == id }
    }

    fun getRecipesByCategory(category: String): List<Recipe> {
        return recipeList
            .filter { it.category.equals(category, ignoreCase = true)}
            .sortedWith(compareBy { it.name.lowercase() })
    }

    //Test data
    private fun loadTestData() {
        // Breakfast
        addRecipe( "Scrambled Eggs", "Breakfast",  "Eggs, Butter, Salt, Pepper", "Whisk eggs, melt butter in pan, cook on low heat")
        addRecipe( "Pancakes", "Breakfast",  "Flour, Milk, Eggs, Baking Powder, Sugar", "Mix dry and wet ingredients, cook on griddle")
        addRecipe( "Avocado Toast", "Breakfast",  "Bread, Avocado, Lemon, Salt, Red Pepper Flakes", "Toast bread, mash avocado, season and spread")

        // Lunch
        addRecipe( "Grilled Cheese", "Lunch",  "Bread, Cheese, Butter", "Butter bread, add cheese, grill until golden")
        addRecipe( "Caesar Salad", "Lunch",  "Romaine, Croutons, Parmesan, Caesar Dressing", "Toss romaine with dressing, top with croutons")
        addRecipe( "Tomato Soup", "Lunch",  "Tomatoes, Onion, Garlic, Cream, Broth", "Saute onion and garlic, add tomatoes, blend smooth")

        //Dinner
        addRecipe("Pasta Marinara", "Dinner",  "Pasta, Marinara Sauce, Garlic, Olive Oil", "Boil pasta, saute garlic, toss with sauce")
        addRecipe("Grilled Chicken", "Dinner",  "Chicken Breast, Olive Oil, Garlic, Spices", "Marinate chicken, grill 6 minutes per side")
        addRecipe("Beef Tacos", "Dinner",  "Ground Beef, Taco Shells, Cheese, Lettuce, Salsa", "Brown beef with seasoning, assemble tacos")

        // Dessert
        addRecipe( "Brownies", "Dessert",  "Chocolate, Butter, Sugar, Eggs, Flour", "Melt chocolate and butter, mix in remaining ingredients, bake")
        addRecipe( "Chocolate Chip Cookies", "Dessert",  "Flour, Butter, Sugar, Eggs, Chocolate Chips", "Cream butter and sugar, mix in remaining, bake at 375")
        addRecipe("Vanilla Ice Cream", "Dessert",  "Heavy Cream, Milk, Sugar, Vanilla Extract, Egg Yolks", "Mix ingredients, churn in ice cream maker, freeze")

    }
}