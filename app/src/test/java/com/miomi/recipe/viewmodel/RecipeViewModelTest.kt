package com.miomi.recipe.viewmodel

import com.miomi.recipe.FakeRecipeRepository
import com.miomi.recipe.model.Recipe
import com.miomi.recipe.util.MainDispatcherRule
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RecipeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeRecipeRepository: FakeRecipeRepository
    private lateinit var viewModel: RecipeViewModel

    @Before
    fun setup(){
        fakeRecipeRepository = FakeRecipeRepository()
        viewModel = RecipeViewModel(fakeRecipeRepository)
    }

    @Test
    fun toggleFavoriteTest(){
        fakeRecipeRepository.seedRecipes(
            Recipe(recipeId = 1, name = "Pasta", category = "Dinner", isFavorite = true)
        )
        viewModel.toggleFavorite(recipeId = 1, isFavorite = true)

        val updated = fakeRecipeRepository.recipes.first {it.recipeId == 1}

        assertFalse(updated.isFavorite)
    }

    @Test
    fun addRecipeTest(){
        val formState = AddRecipeFormState(
            recipeName = "Pasta",
            category = "Dinner",
            ingredients = listOf(
                IngredientEntry(name = "Spaghetti", quantity = "2", unit = "cups"),
                IngredientEntry(name = "Sauce", quantity = "1", unit = "jar")
            ),
            steps = listOf(
                StepEntry(description = "Boil water"),
                StepEntry(description = "Cook noodles"),
                StepEntry(description = "Add sauce")
            )
        )

        viewModel.addRecipe(formState)

        assertEquals(1, fakeRecipeRepository.recipes.size)
        assertEquals("Pasta", fakeRecipeRepository.recipes[0].name)


        assertEquals(2, fakeRecipeRepository.ingredients.size)
        assertTrue(fakeRecipeRepository.ingredients.all { it.recipeId == fakeRecipeRepository.recipes[0].recipeId })

        assertEquals(3, fakeRecipeRepository.steps.size)
        assertEquals(1, fakeRecipeRepository.steps[0].sequenceNum)
        assertEquals(2, fakeRecipeRepository.steps[1].sequenceNum)
        assertEquals(3, fakeRecipeRepository.steps[2].sequenceNum)
    }


    @Test
    fun updateRecipeTest(){

        fakeRecipeRepository.seedRecipes(
            Recipe(recipeId = 1, name = "Old spaghetti", "Dinner", isFavorite = false)
        )

        val formState = AddRecipeFormState(
            recipeId = 1,
            recipeName = "New Pasta",
            category = "Dinner",
            isFavorite = false,
            ingredients = listOf(
                IngredientEntry(name = "Noodles", quantity = "2", unit = "cups")
            ),
            steps = listOf(
                StepEntry(description = "Boil water"),
                StepEntry(description = "Cook noodles")
            )
        )

        viewModel.updateRecipe(formState)

        assertEquals("New Pasta", fakeRecipeRepository.recipes.first { it.recipeId == 1 }.name)

        assertEquals(1, fakeRecipeRepository.ingredients.size)
        assertEquals("Noodles", fakeRecipeRepository.ingredients[0].name)

        assertEquals(2, fakeRecipeRepository.steps.size)
        assertEquals(1, fakeRecipeRepository.steps[0].sequenceNum)
        assertEquals(2, fakeRecipeRepository.steps[1].sequenceNum)
    }


    @Test
    fun updateRecipeWithNullRecipeId() {
        val formState = AddRecipeFormState(
            recipeId = null,  //this should trigger an early return and nothing should be updated
            recipeName = "Ghost Recipe",
            category = "Dinner"
        )

        viewModel.updateRecipe(formState)

        // everything should still be empty
        assertTrue(fakeRecipeRepository.recipes.isEmpty())
        assertTrue(fakeRecipeRepository.ingredients.isEmpty())
        assertTrue(fakeRecipeRepository.steps.isEmpty())
    }
}