package com.miomi.recipe.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class AddRecipeFormState(
    val recipeName: String = "",
    val category: String = "",
    val ingredients: List<IngredientEntry> = listOf((IngredientEntry())),
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

    fun clearForm() {
        formState = AddRecipeFormState()
    }

    fun isDetailsValid() = formState.recipeName.isNotBlank() && formState.category.isNotBlank()

    fun isIngredientsValid() = formState.ingredients.all {
        it.name.isNotBlank() && it.quantity.isNotBlank() && it.unit.isNotBlank()
    }

    fun isStepsValid() = formState.steps.all { it.description.isNotBlank() }


}