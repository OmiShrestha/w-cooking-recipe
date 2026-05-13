package com.miomi.recipe.data.dao


import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.miomi.recipe.data.RecipeDatabase
import com.miomi.recipe.model.Ingredient
import com.miomi.recipe.model.Recipe
import com.miomi.recipe.model.Step
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipeDaoTest {

    private lateinit var db: RecipeDatabase
    private lateinit var recipeDao: RecipeDao

    @Before
    fun setup() {
        val context =
            ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, RecipeDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        recipeDao = db.recipeDao()
    }

    @Test
    @Throws(Exception::class)
    fun insertRecipeTest() = runBlocking {
        val recipe = Recipe(name = "Pasta", category = "Dinner", isFavorite = false)
        recipeDao.insertRecipe(recipe)

        val recipes = recipeDao.getAllRecipes().first()

        assertEquals(1, recipes.size)
        assertEquals("Pasta", recipes[0].name)
    }

    @Test
    @Throws(Exception::class)
    fun updateIsFavoriteTest() = runBlocking {
        val recipeId = recipeDao.insertRecipe(
            Recipe(name = "Pasta", category = "Dinner", isFavorite = false)
        ).toInt()

        recipeDao.updateIsFavorite(recipeId, true)

        val favorites = recipeDao.getFavoriteRecipes().first()
        assertEquals(1, favorites.size)
        assertTrue(favorites[0].isFavorite)
    }


    @Test
    @Throws(Exception::class)
    fun getFavoriteRecipesTest() = runBlocking {
        recipeDao.insertRecipe(Recipe(name = "Pasta", category = "Dinner", isFavorite = false))
        recipeDao.insertRecipe(Recipe(name = "Pizza", category = "Dinner", isFavorite = true))

        val favorites = recipeDao.getFavoriteRecipes().first()

        assertEquals(1, favorites.size)
        assertEquals("Pizza", favorites[0].name)
    }

    @Test
    @Throws(Exception::class)
    fun getRecipeWithDetailsTest() = runBlocking {
        val recipeId = recipeDao.insertRecipe(
            Recipe(name = "Cake", category = "Dessert", isFavorite = false)
        ).toInt()

            //TODO: Im not sure if its good practice to use other DAOs in another DAOs test class
        db.ingredientDao().insertIngredient(
            Ingredient(recipeId = recipeId, name = "Flour", quantity = 2.0, unit = "cups")
        )
        db.stepDao().insertStep(
            Step(recipeId = recipeId, sequenceNum = 1, step = "Add")
        )

        val result = recipeDao.getRecipeWithDetails(recipeId).first()

        assertEquals("Cake", result.recipe.name)
        assertEquals(1, result.ingredients.size)
        assertEquals("Flour", result.ingredients[0].name)
        assertEquals(1, result.steps.size)
        assertEquals("Add", result.steps[0].step)
    }

    @After
    fun teardown() {
        db.close()
    }
}