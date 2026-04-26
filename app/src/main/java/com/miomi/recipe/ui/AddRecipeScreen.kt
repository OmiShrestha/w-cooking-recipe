package com.miomi.recipe.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.miomi.recipe.viewmodel.RecipeViewModel
import kotlin.text.isBlank
import kotlin.text.isNotEmpty

data class RecipeFormState(
    val name: String = "",
    val category: String = "",
    val ingredients: String = "",
    val instructions: String = ""
)

//Custom saver needed to remember a data class
val RecipeFormStateSaver = Saver<RecipeFormState, List<String>>(
    save = { listOf(it.name, it.category, it.ingredients, it.instructions) },
    restore = { RecipeFormState(it[0], it[1], it[2], it[3]) }
)


//AddRecipeScreen exceeds 40 lines due to Scaffold and Column boilerplate
//However all logic is delegated to the children composables and onSaveRecipe helper function
//This was as short as I could get it
@Composable
fun AddRecipeScreen(navController: NavController, viewModel: RecipeViewModel) {
    var state by rememberSaveable(stateSaver = RecipeFormStateSaver) {
        mutableStateOf(RecipeFormState())
    }

    var errorMessage by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                },
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            ScreenTitle()

            RecipeDetailsSection(
                state = state,
                focusManager = focusManager,
                categories = viewModel.getCategories(),
                onStateChange = { state = it }
                )

            HorizontalDivider()

            IngredientsSection(
                state = state,
                focusManager = focusManager,
                onStateChange = { state = it }
            )

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            FormButtons(
                onCancel = {
                    focusManager.clearFocus()
                    navController.popBackStack()
                },
                onSave = {
                    focusManager.clearFocus()
                    errorMessage = onSaveRecipe(state, viewModel, navController)
                }
            )
        }
    }
}

@Composable
private fun ScreenTitle() {
    Text(
        "Add Recipe",
        style = MaterialTheme.typography.headlineLarge
    )

    HorizontalDivider(
        modifier = Modifier.padding(bottom = 4.dp),
        thickness = 2.dp
    )
}

@Composable
private fun IngredientsSection(
    state: RecipeFormState,
    focusManager: FocusManager,
    onStateChange: (RecipeFormState) -> Unit
) {
    Text(
        text = "Ingredients & Instructions",
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.secondary
    )
    OutlinedTextField(
        value = state.ingredients,
        onValueChange = { onStateChange(state.copy(ingredients = it)) },
        label = { Text("Ingredients") },
        modifier = Modifier.fillMaxWidth(),
        minLines = 3,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
    )
    OutlinedTextField(
        value = state.instructions,
        onValueChange =  { onStateChange(state.copy(instructions = it)) },
        label = { Text("Instructions") },
        modifier = Modifier.fillMaxWidth(),
        minLines = 3,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
    )
}

private fun onSaveRecipe(
    state: RecipeFormState,
    viewModel: RecipeViewModel,
    navController: NavController
): String {
    if (state.name.isBlank() || state.category.isBlank() ||
        state.ingredients.isBlank() || state.instructions.isBlank()) {
        return "All fields are required"
    }
    viewModel.addRecipe(state.name, state.category, state.ingredients, state.instructions)
    navController.popBackStack()
    return ""
}