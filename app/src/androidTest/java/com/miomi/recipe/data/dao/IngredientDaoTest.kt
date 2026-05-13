package com.miomi.recipe.data.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.miomi.recipe.data.RecipeDatabase
import com.miomi.recipe.model.Ingredient
import com.miomi.recipe.model.Recipe
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IngredientDaoTest {

    private lateinit var db: RecipeDatabase
    private lateinit var recipeDao: RecipeDao
    private lateinit var ingredientDao: IngredientDao

    @Before
    fun setup() {
        val context =
            ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, RecipeDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        recipeDao = db.recipeDao()
        ingredientDao = db.ingredientDao()
    }

    @Test
    @Throws(Exception::class)
    fun insertIngredientTest() = runBlocking {
        //Just checking whether
        //insertRecipe() has already been unit tested
        val recipe = Recipe(name = "Pasta", category = "Dinner", isFavorite = false)
        val recipeId = recipeDao.insertRecipe(recipe).toInt()

        ingredientDao.insertIngredient(Ingredient(recipeId = recipeId, name = "bananas", quantity = 3.3, unit = "slices"))

        val ingredientsForRecipe = ingredientDao.getAllIngredientsForRecipe(recipeId).first()

        assertEquals(1, ingredientsForRecipe.size)
        assertEquals("bananas", ingredientsForRecipe[0].name)
        assertEquals(3.3, ingredientsForRecipe[0].quantity)
        assertEquals("slices", ingredientsForRecipe[0].unit)
    }

    @Test
    @Throws(Exception::class)
    fun getAllIngredientsForRecipeTest() = runBlocking {

        //insertRecipe() has already been unit tested
        val recipe = Recipe(name = "Pasta", category = "Dinner", isFavorite = false)
        val recipeId = recipeDao.insertRecipe(recipe).toInt()

        ingredientDao.insertIngredient(Ingredient(recipeId = recipeId, name = "bananas", quantity = 3.3, unit = "slices"))
        ingredientDao.insertIngredient(Ingredient(recipeId = recipeId, name = "melons", quantity = 3.4, unit = "rings"))
        ingredientDao.insertIngredient(Ingredient(recipeId = recipeId, name = "apples", quantity = 3.5, unit = "peels"))

        val ingredientsForRecipe = ingredientDao.getAllIngredientsForRecipe(recipeId).first()

        assertEquals(3, ingredientsForRecipe.size)
        assertEquals("bananas", ingredientsForRecipe[0].name)
        assertEquals(3.3, ingredientsForRecipe[0].quantity)
        assertEquals("slices", ingredientsForRecipe[0].unit)

        assertEquals("melons", ingredientsForRecipe[1].name)
        assertEquals(3.4, ingredientsForRecipe[1].quantity)
        assertEquals("rings", ingredientsForRecipe[1].unit)

        assertEquals("apples", ingredientsForRecipe[2].name)
        assertEquals(3.5, ingredientsForRecipe[2].quantity)
        assertEquals("peels", ingredientsForRecipe[2].unit)

    }

    @Test
    @Throws(Exception::class)
    fun deleteIngredientTest() = runBlocking {
        val recipeId = recipeDao.insertRecipe(
            Recipe(name = "Pasta", category = "Dinner", isFavorite = false)
        ).toInt()

        val ingredientId = ingredientDao.insertIngredient(
            Ingredient(recipeId = recipeId, name = "bananas", quantity = 3.3, unit = "slices")
        ).toInt()
        val ingredient = Ingredient(ingredientId = ingredientId, recipeId = recipeId, name = "bananas", quantity = 3.3, unit = "slices")

        ingredientDao.deleteIngredient(ingredient)

        val ingredients = ingredientDao.getAllIngredientsForRecipe(recipeId).first()
        assertEquals(0, ingredients.size)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAllIngredientsForRecipeTest() = runBlocking {
        val recipeId1 = recipeDao.insertRecipe(
            Recipe(name = "ice cream", category = "dessert", isFavorite = false)
        ).toInt()
        val recipeId2 = recipeDao.insertRecipe(
            Recipe(name = "salad", category = "Dinner", isFavorite = false)
        ).toInt()

        ingredientDao.insertIngredient(Ingredient(recipeId = recipeId1, name = "bananas", quantity = 3.3, unit = "slices"))
        ingredientDao.insertIngredient(Ingredient(recipeId = recipeId1, name = "apples", quantity = 1.0, unit = "whole"))
        ingredientDao.insertIngredient(Ingredient(recipeId = recipeId2, name = "flour", quantity = 2.0, unit = "cups"))

        ingredientDao.deleteAllIngredientsForRecipe(recipeId1)

        val recipe1Ingredients = ingredientDao.getAllIngredientsForRecipe(recipeId1).first()
        val recipe2Ingredients = ingredientDao.getAllIngredientsForRecipe(recipeId2).first()


        assertEquals(0, recipe1Ingredients.size)
        //verify that only the correct recipeId is deleted
        assertEquals(1, recipe2Ingredients.size)
        assertEquals("flour", recipe2Ingredients[0].name)
    }

    @After
    fun teardown() {
        db.close()
    }
}