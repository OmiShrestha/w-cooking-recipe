package com.miomi.recipe.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.miomi.recipe.data.repository.RecipeRepository

/**
 * This ViewModel is scoped only to the create_recipe nested navGraph
 * It manages the AddRecipeFormState across the three separate recipe creation screens
 */

//This data class is distinct from the recipe room entity and represents the UI state for the recipe creation flow.
data class AddRecipeFormState(
    val recipeId: Int? = null,
    val isFavorite: Boolean = false,
    val recipeName: String = "",
    val category: String = "",
    val ingredients: List<IngredientEntry> = listOf(IngredientEntry()),
    val steps: List<StepEntry> = listOf(StepEntry())
)

data class IngredientEntry(
    val name: String = "",
    val quantity: String = "",
    val unit: String = ""

)

data class StepEntry(
    val description: String = ""
)

class AddRecipeViewModel : ViewModel() {
    var formState by mutableStateOf(AddRecipeFormState())
        private set

    fun getCategories() = RecipeRepository.categories

    fun updateRecipeName(recipeName: String){
        formState = formState.copy(recipeName = recipeName)
    }

    fun updateCategory(category: String){
        formState = formState.copy(category = category)
    }

    fun addIngredient() {
        formState = formState.copy(ingredients = formState.ingredients + IngredientEntry())
    }

    fun updateIngredient(index: Int, entry: IngredientEntry){
        val updated = formState.ingredients.toMutableList()
        updated[index] = entry
        formState = formState.copy(ingredients = updated)
    }

    fun removeIngredient(index: Int){
        val updated = formState.ingredients.toMutableList()
        updated.removeAt(index)
        formState = formState.copy(ingredients = updated)
    }

    fun updateStep(index: Int, entry: StepEntry) {
        val updated = formState.steps.toMutableList()
        updated[index] = entry
        formState = formState.copy(steps = updated)
    }

    fun addStep() {
        formState = formState.copy(steps = formState.steps + StepEntry())
    }

    fun removeStep(index: Int) {
        val updated = formState.steps.toMutableList()
        updated.removeAt(index)
        formState = formState.copy(steps = updated)
    }

    // loads the existing recipe data into the form state for editing
    fun loadExisting(
        recipeId: Int,
        name: String,
        category: String,
        isFavorite: Boolean,
        ingredients: List<IngredientEntry>,
        steps: List<StepEntry>
    ) {
        formState = AddRecipeFormState(
            recipeId = recipeId,
            isFavorite = isFavorite,
            recipeName = name,
            category = category,
            ingredients = ingredients,
            steps = steps
        )
    }

    val isEditing: Boolean get() = formState.recipeId != null

    fun clearForm() {
        formState = AddRecipeFormState()
    }

    fun isDetailsValid() = formState.recipeName.isNotBlank() && formState.category.isNotBlank()

    fun isIngredientsValid() = formState.ingredients.all {
        it.name.isNotBlank() && it.quantity.isNotBlank() && it.unit.isNotBlank()
    }

    fun isStepsValid() = formState.steps.all { it.description.isNotBlank() }

}