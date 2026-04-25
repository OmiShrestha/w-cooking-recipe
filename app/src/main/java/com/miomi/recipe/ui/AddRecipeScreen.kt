package com.miomi.recipe.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import kotlin.collections.forEach
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
private fun RecipeDetailsSection(
    state: RecipeFormState,
    focusManager: FocusManager,
    categories: List<String>,
    onStateChange: (RecipeFormState) -> Unit
) {
    Text(
        text = "Recipe Details",
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.secondary
    )
    OutlinedTextField(
        value = state.name,
        onValueChange = { onStateChange(state.copy(name = it)) },
        label = { Text("Recipe Name") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
    )
    CategoryDropdown(
        selectedCategory = state.category,
        focusManager = focusManager,
        categories = categories,
        onCategoryChange = { onStateChange(state.copy(category = it)) }
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

@Composable
private fun CategoryDropdown(
    selectedCategory: String,
    focusManager: FocusManager,
    categories: List<String>,
    onCategoryChange: (String) -> Unit
) {
    //Permits the Dropdown to be closed in the event of a recomp. I think this makes sense from a UX perspective
    var categoryExpanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedCategory,
            onValueChange = {},
            readOnly = true,
            label = { Text("Category") },
            trailingIcon = { DropdownArrow(categoryExpanded) { categoryExpanded = !categoryExpanded } },
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable {
                    focusManager.clearFocus()
                    categoryExpanded = !categoryExpanded
                }
        )
        CategoryDropdownMenu(
            expanded = categoryExpanded,
            categories = categories,
            onDismiss = { categoryExpanded = false },
            onCategoryChange = {
                onCategoryChange(it)
                categoryExpanded = false
            }
        )
    }
}

@Composable
private fun DropdownArrow(expanded: Boolean, onClick: () -> Unit) {
    Icon(
        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
        contentDescription = null,
        modifier = Modifier.clickable { onClick() }
    )
}

@Composable
private fun CategoryDropdownMenu(
    expanded: Boolean,
    categories: List<String>,
    onDismiss: () -> Unit,
    onCategoryChange: (String) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = Modifier.width(200.dp)
    ) {
        categories.forEach { option ->
            DropdownMenuItem(
                text = { Text(option) },
                onClick = { onCategoryChange(option) }
            )
        }
    }
}

@Composable
private fun FormButtons(onCancel: () -> Unit, onSave: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                "Cancel",
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Button(
            onClick = onSave,
            modifier = Modifier.weight(1f)
        ) {
            Text("Save Recipe")
        }
    }
}