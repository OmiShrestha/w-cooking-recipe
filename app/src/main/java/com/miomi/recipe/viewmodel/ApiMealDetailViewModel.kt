package com.miomi.recipe.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.miomi.recipe.RecipeApplication
import com.miomi.recipe.data.repository.RecipeRepository
import com.miomi.recipe.model.Ingredient
import com.miomi.recipe.model.Recipe
import com.miomi.recipe.model.Step
import com.miomi.recipe.network.model.MealDto
import com.miomi.recipe.network.repository.MealRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ViewModel for managing the state of the API meal detail screen
data class ApiMealDetailUiState(
    val meal: MealDto? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val isSaved: Boolean = false
)

// ViewModel for managing the state of the API meal detail screen
class ApiMealDetailViewModel(
    private val mealRepository: MealRepository,
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ApiMealDetailUiState())
    val uiState: StateFlow<ApiMealDetailUiState> = _uiState.asStateFlow()

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as RecipeApplication
                ApiMealDetailViewModel(app.mealRepository, app.recipeRepository)
            }
        }
    }

    fun loadMeal(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            mealRepository.lookupById(id).fold(
                onSuccess = { meal ->
                    _uiState.update { it.copy(isLoading = false, meal = meal) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message ?: "Failed to load recipe")
                    }
                }
            )
        }
    }

    fun saveMeal() {
        val meal = _uiState.value.meal ?: return
        viewModelScope.launch {
            val recipeId = recipeRepository.insertRecipe(
                Recipe(name = meal.strMeal, category = meal.strCategory ?: "Other", isFavorite = false)
            )
            meal.getIngredients().forEach { (name, measure) ->
                val (quantity, unit) = parseMeasure(measure)
                recipeRepository.insertIngredient(
                    Ingredient(recipeId = recipeId.toInt(), name = name, quantity = quantity, unit = unit)
                )
            }
            val stepLabelRegex = Regex("^step\\s*\\d+\\.?$", RegexOption.IGNORE_CASE)
            meal.strInstructions
                ?.split("\r\n", "\n")
                ?.map { it.trim() }
                ?.filter { it.isNotBlank() && !it.matches(stepLabelRegex) }
                ?.forEachIndexed { index, step ->
                    recipeRepository.insertStep(
                        Step(recipeId = recipeId.toInt(), sequenceNum = index + 1, step = step)
                    )
                }
            _uiState.update { it.copy(isSaved = true) }
        }
    }

    private fun parseMeasure(measure: String): Pair<Double, String> {
        val parts = measure.trim().split(Regex("\\s+"), limit = 2)
        val qty = parts.getOrElse(0) { "" }.toDoubleOrNull()
        return if (qty != null) qty to parts.getOrElse(1) { "" } else 1.0 to measure.trim()
    }
}
