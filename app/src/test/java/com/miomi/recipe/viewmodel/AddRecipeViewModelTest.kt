package com.miomi.recipe.viewmodel

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test

class AddRecipeViewModelTest {

    private lateinit var viewModel : AddRecipeViewModel

    @Before
    fun setup() {
        viewModel = AddRecipeViewModel()
    }

    @Test
    fun loadExistingTest() {
        viewModel.loadExisting(
            recipeId = 1,
            name = "Spaghetti",
            category = "Dinner",
            isFavorite = true,
            ingredients = listOf(IngredientEntry(name = "Noodles", quantity = "2", unit = "cups")),
            steps = listOf(StepEntry(description = "Boil water"))
        )

        assertEquals(1, viewModel.formState.recipeId)
        assertEquals("Spaghetti", viewModel.formState.recipeName)
        assertEquals("Dinner", viewModel.formState.category)
        assertTrue(viewModel.formState.isFavorite)
        assertEquals(1, viewModel.formState.ingredients.size)
        assertEquals(1, viewModel.formState.steps.size)
    }

    @Test
    fun isEditingTestRecipeIdSet() {
        //should return true when recipeId is set
        viewModel.loadExisting(1, "Spaghetti", "Dinner", false, emptyList(), emptyList())
        assertTrue(viewModel.isEditing)
    }

    @Test
    fun isEditingTestRecipeIdNotSet() {
        //should return false when recipeId is not set
        assertFalse(viewModel.isEditing)
    }

    @Test
    fun isDetailsValidTestWithoutName(){
        viewModel.updateCategory("Spaghetti")
        //isDetailsValid() should return false without recipe name
        assertFalse(viewModel.isDetailsValid())
    }

    @Test
    fun isDetailsValidTestWithName(){
        viewModel.updateRecipeName("Spaghetti")
        viewModel.updateCategory("Dinner")
        assertTrue(viewModel.isDetailsValid())
    }

    @Test
    fun isIngredientsValidTestWithoutQuantity() {
        viewModel.updateIngredient(0, IngredientEntry(name = "Spaghetti noodles", quantity = "", unit = "cups"))
        //isIngredientsValid() should return false blank quantity
        assertFalse(viewModel.isIngredientsValid())
    }

    @Test
    fun isIngredientsValidTestWithoutUnit() {
        viewModel.updateIngredient(0, IngredientEntry(name = "Spaghetti noodles", quantity = "2", unit = ""))
        //Should return false when unit is blank
        assertFalse(viewModel.isIngredientsValid())
    }

    @Test
    fun isIngredientsValidTest() {
        viewModel.updateIngredient(0, IngredientEntry(name = "Spaghetti noodles", quantity = "2", unit = "cups"))
        assertTrue(viewModel.isIngredientsValid())
    }

    @Test
    fun isStepsValidWithoutDescription() {
        viewModel.updateStep(0, StepEntry(description = ""))
        //should return false when description is blank
        assertFalse(viewModel.isStepsValid())
    }

    @Test
    fun isStepsValidTest() {
        viewModel.updateStep(0, StepEntry(description = "Boil water"))
        assertTrue(viewModel.isStepsValid())
    }


}