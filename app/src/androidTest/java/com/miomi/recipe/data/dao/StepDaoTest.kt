package com.miomi.recipe.data.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.miomi.recipe.data.RecipeDatabase
import com.miomi.recipe.model.Recipe
import com.miomi.recipe.model.Step
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StepDaoTest {

    private lateinit var db: RecipeDatabase
    private lateinit var stepDao: StepDao
    private lateinit var recipeDao: RecipeDao


    @Before
    fun setup() {
        val context =
            ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, RecipeDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        stepDao = db.stepDao()
        recipeDao = db.recipeDao()
    }

    @Test
    @Throws(Exception::class)
    fun insertStepTest() = runBlocking {
        //insertRecipe() has already been unit tested
        val recipeId = recipeDao.insertRecipe(
            Recipe(name = "pork", category = "dinner", isFavorite = false)
        ).toInt()

        stepDao.insertStep(Step(recipeId = recipeId, sequenceNum = 1, step = "chop celery"))

        val steps = stepDao.getStepsForRecipe(recipeId).first()

        assertEquals(1, steps.size)

        assertEquals(1, steps[0].sequenceNum)
        assertEquals("chop celery", steps[0].step)
    }

    @Test
    @Throws(Exception::class)
    fun getStepsForRecipeOrderTest() = runBlocking {
        val recipeId = recipeDao.insertRecipe(
            Recipe(name = "pork", category = "dinner", isFavorite = false)
        ).toInt()

        //insert out of order, but should return in order
        stepDao.insertStep(Step(recipeId = recipeId, sequenceNum = 3, step = "chop celery"))
        stepDao.insertStep(Step(recipeId = recipeId, sequenceNum = 1, step = "chop banana"))
        stepDao.insertStep(Step(recipeId = recipeId, sequenceNum = 2, step = "chop lemon"))
        stepDao.insertStep(Step(recipeId = recipeId, sequenceNum = 4, step = "chop pineapple"))

        val steps = stepDao.getStepsForRecipe(recipeId).first()

        assertEquals(4, steps.size)

        assertEquals(1, steps[0].sequenceNum)
        assertEquals("chop banana", steps[0].step)

        assertEquals(2, steps[1].sequenceNum)
        assertEquals("chop lemon", steps[1].step)

        assertEquals(3, steps[2].sequenceNum)
        assertEquals("chop celery", steps[2].step)

        assertEquals(4, steps[3].sequenceNum)
        assertEquals("chop pineapple", steps[3].step)
    }

    @Test
    @Throws(Exception::class)
    fun deleteStepTest() = runBlocking {
        val recipeId = recipeDao.insertRecipe(
            Recipe(name = "Pasta", category = "Dinner", isFavorite = false)
        ).toInt()



        stepDao.insertStep(Step(recipeId = recipeId, sequenceNum = 1, step = "chop celery"))
        stepDao.insertStep(Step(recipeId = recipeId, sequenceNum = 2, step = "chop banana"))
        val stepId = stepDao.insertStep(Step(recipeId = recipeId, sequenceNum = 3, step = "chop lemon")).toInt()
        stepDao.insertStep(Step(recipeId = recipeId, sequenceNum = 4, step = "chop pineapple"))

        val step3 = Step(stepId = stepId, recipeId = recipeId, sequenceNum = 3, step = "chop lemon")
        stepDao.deleteStep(step3)

        val allStepsForRecipe = stepDao.getStepsForRecipe(recipeId).first()

        assertEquals(3, allStepsForRecipe.size)
        assertEquals(1, allStepsForRecipe[0].sequenceNum)
        assertEquals("chop celery", allStepsForRecipe[0].step)
        assertEquals(2, allStepsForRecipe[1].sequenceNum)
        assertEquals("chop banana", allStepsForRecipe[1].step)
        assertEquals(4, allStepsForRecipe[2].sequenceNum)
        assertEquals("chop pineapple", allStepsForRecipe[2].step)

    }

    @Test
    @Throws(Exception::class)
    fun deleteAllStepsForRecipeTest() = runBlocking {
        val recipeId1 = recipeDao.insertRecipe(
            Recipe(name = "ice cream", category = "dessert", isFavorite = false)
        ).toInt()
        val recipeId2 = recipeDao.insertRecipe(
            Recipe(name = "salad", category = "Dinner", isFavorite = false)
        ).toInt()

        stepDao.insertStep(Step(recipeId = recipeId1, sequenceNum = 1, step = "chop celery"))
        stepDao.insertStep(Step(recipeId = recipeId2, sequenceNum = 1, step = "chop banana"))
        stepDao.insertStep(Step(recipeId = recipeId1, sequenceNum = 2, step = "chop lemon"))
        stepDao.insertStep(Step(recipeId = recipeId2, sequenceNum = 2, step = "chop pineapple"))

        stepDao.deleteAllStepsForRecipe(recipeId1)

        val recipe1Steps = stepDao.getStepsForRecipe(recipeId1).first()
        val recipe2Steps = stepDao.getStepsForRecipe(recipeId2).first()


        assertEquals(0, recipe1Steps.size)

        //verify that only the correct recipeId is deleted
        assertEquals(2, recipe2Steps.size)
        assertEquals(1, recipe2Steps[0].sequenceNum)
        assertEquals("chop banana", recipe2Steps[0].step)
        assertEquals(2, recipe2Steps[1].sequenceNum)
        assertEquals("chop pineapple", recipe2Steps[1].step)
    }

    @Test
    @Throws(Exception::class)
    fun getStepsForRecipeEmptyTest() = runBlocking {
        val recipeId = recipeDao.insertRecipe(
            Recipe(name = "Pasta", category = "Dinner", isFavorite = false)
        ).toInt()

        val steps = stepDao.getStepsForRecipe(recipeId).first()

        assertEquals(0, steps.size)
    }


    @After
    fun teardown() {
        db.close()
    }
}