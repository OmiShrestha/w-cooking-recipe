package com.miomi.recipe.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.miomi.recipe.RecipeApplication
import com.miomi.recipe.network.model.MealDto
import com.miomi.recipe.network.repository.MealRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ViewModel for managing the state of the API search screen
enum class SearchType(val label: String, val placeholder: String) {
    NAME("Name", "e.g. Chicken"),
    CATEGORY("Category", "e.g. Seafood"),
    INGREDIENT("Ingredient", "e.g. Garlic")
}

// ViewModel for managing the state of the API search screen
data class ApiSearchUiState(
    val query: String = "",
    val searchType: SearchType = SearchType.NAME,
    val results: List<MealDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasSearched: Boolean = false
)

// ViewModel for managing the state of the API search screen
class ApiSearchViewModel(private val mealRepository: MealRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ApiSearchUiState())
    val uiState: StateFlow<ApiSearchUiState> = _uiState.asStateFlow()

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as RecipeApplication
                ApiSearchViewModel(app.mealRepository)
            }
        }
    }

    fun onQueryChanged(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    fun onSearchTypeChanged(type: SearchType) {
        _uiState.update { it.copy(searchType = type, results = emptyList(), error = null, hasSearched = false) }
    }

    fun search() {
        val query = _uiState.value.query.trim()
        if (query.isBlank()) return
        _uiState.update { it.copy(isLoading = true, error = null, results = emptyList(), hasSearched = true) }
        viewModelScope.launch {
            val result = when (_uiState.value.searchType) {
                SearchType.NAME -> mealRepository.searchByName(query)
                SearchType.CATEGORY -> mealRepository.filterByCategory(query)
                SearchType.INGREDIENT -> mealRepository.filterByIngredient(query)
            }
            result.fold(
                onSuccess = { meals ->
                    _uiState.update { it.copy(isLoading = false, results = meals) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message ?: "Search failed")
                    }
                }
            )
        }
    }
}
