// Author: Omi Shrestha

package com.miomi.recipe.viewmodel

import com.miomi.recipe.FakeMealRepository
import com.miomi.recipe.network.model.MealDto
import com.miomi.recipe.util.MainDispatcherRule
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

// Unit tests for ApiSearchViewModel
class ApiSearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeMealRepository: FakeMealRepository
    private lateinit var viewModel: ApiSearchViewModel

    @Before
    fun setup() {
        fakeMealRepository = FakeMealRepository()
        viewModel = ApiSearchViewModel(fakeMealRepository)
    }

    // Test 1: To check that searching with a blank query does not trigger a search and leaves the state unchanged.
    @Test
    fun searchWithBlankQueryDoesNothing() {
        viewModel.onQueryChanged("   ")
        viewModel.search()

        assertFalse(viewModel.uiState.value.hasSearched)
        assertFalse(viewModel.uiState.value.isLoading)
        assertTrue(viewModel.uiState.value.results.isEmpty())
    }

    // Test 2: To verify that a successful search by name updates the UI state with the expected results and clears any previous errors.
    @Test
    fun searchByNameSuccess() {
        val meal = fakeMeal(id = "1", name = "Pasta")
        fakeMealRepository.searchByNameResult = Result.success(listOf(meal))

        viewModel.onQueryChanged("Pasta")
        viewModel.search()

        val state = viewModel.uiState.value
        assertEquals(1, state.results.size)
        assertEquals("Pasta", state.results[0].strMeal)
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertTrue(state.hasSearched)
    }

    // Test 3: To verify that a failed search by name sets the error message in the UI state.
    @Test
    fun searchByNameFailureSetsError() {
        fakeMealRepository.searchByNameResult = Result.failure(Exception("Network error"))

        viewModel.onQueryChanged("Pasta")
        viewModel.search()

        val state = viewModel.uiState.value
        assertEquals("Network error", state.error)
        assertTrue(state.results.isEmpty())
        assertFalse(state.isLoading)
    }
    
    // Test 4: To ensure that the correct repository method is called based on the selected search type (name, category, ingredient) and that the query is passed correctly.
    @Test
    fun searchDispatchesCorrectMethodPerSearchType() {
        viewModel.onQueryChanged("Seafood")
        viewModel.onSearchTypeChanged(SearchType.CATEGORY)
        viewModel.search()

        assertEquals("Seafood", fakeMealRepository.lastFilterByCategoryQuery)
        assertNull(fakeMealRepository.lastSearchByNameQuery)
        assertNull(fakeMealRepository.lastFilterByIngredientQuery)
    }

    // Test 5: To verify that changing the search type resets the results and clears any previous errors.
    @Test
    fun changingSearchTypeResetsResults() {
        val meal = fakeMeal(id = "1", name = "Pasta")
        fakeMealRepository.searchByNameResult = Result.success(listOf(meal))
        viewModel.onQueryChanged("Pasta")
        viewModel.search()

        assertTrue(viewModel.uiState.value.results.isNotEmpty())

        viewModel.onSearchTypeChanged(SearchType.INGREDIENT)

        val state = viewModel.uiState.value
        assertTrue(state.results.isEmpty())
        assertFalse(state.hasSearched)
        assertNull(state.error)
    }

    // Helper function to create a fake MealDto
    // NULL to satisfy all non-nullable fields in MealDto
    private fun fakeMeal(id: String, name: String) = MealDto(
        idMeal = id,
        strMeal = name,
        strCategory = null,
        strArea = null,
        strInstructions = null,
        strMealThumb = null,
        strIngredient1 = null, strIngredient2 = null, strIngredient3 = null,
        strIngredient4 = null, strIngredient5 = null, strIngredient6 = null,
        strIngredient7 = null, strIngredient8 = null, strIngredient9 = null,
        strIngredient10 = null, strIngredient11 = null, strIngredient12 = null,
        strIngredient13 = null, strIngredient14 = null, strIngredient15 = null,
        strIngredient16 = null, strIngredient17 = null, strIngredient18 = null,
        strIngredient19 = null, strIngredient20 = null,
        strMeasure1 = null, strMeasure2 = null, strMeasure3 = null,
        strMeasure4 = null, strMeasure5 = null, strMeasure6 = null,
        strMeasure7 = null, strMeasure8 = null, strMeasure9 = null,
        strMeasure10 = null, strMeasure11 = null, strMeasure12 = null,
        strMeasure13 = null, strMeasure14 = null, strMeasure15 = null,
        strMeasure16 = null, strMeasure17 = null, strMeasure18 = null,
        strMeasure19 = null, strMeasure20 = null
    )
}
