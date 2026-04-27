package com.miomi.recipe.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun RecipeDetailsSection(
    name: String,
    category: String,
    focusManager: FocusManager,
    categories: List<String>,
    onNameChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit
) {
    Text(
        text = "Recipe Details",
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.secondary
    )
    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        label = { Text("Recipe Name") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
    )
    CategoryDropdown(
        selectedCategory = category,
        focusManager = focusManager,
        categories = categories,
        onCategoryChange = onCategoryChange
    )
}

@Composable
fun CategoryDropdown(
    selectedCategory: String,
    focusManager: FocusManager,
    categories: List<String>,
    onCategoryChange: (String) -> Unit
) {
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
fun DropdownArrow(expanded: Boolean, onClick: () -> Unit) {
    Icon(
        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
        contentDescription = null,
        modifier = Modifier.clickable { onClick() }
    )
}

@Composable
fun CategoryDropdownMenu(
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

// reusable dialog for confirming if user wants to discard changes when navigating back from an edit screen with unsaved changes
@Composable
fun DiscardChangesDialog(onDiscard: () -> Unit, onKeepEditing: () -> Unit) {
    AlertDialog(
        onDismissRequest = onKeepEditing,
        title = { Text("Discard changes?") },
        text = { Text("Your changes will be lost if you go back.") },
        confirmButton = {
            TextButton(onClick = onDiscard) { Text("Discard") }
        },
        dismissButton = {
            TextButton(onClick = onKeepEditing) { Text("Keep editing") }
        }
    )
}

@Composable
fun FormButtons(
    onCancel: () -> Unit,
    onSave: () -> Unit,
    cancelLabel: String = "Cancel",
    saveLabel: String = "Save Recipe"
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier.weight(1f)
        ) {
            Text(cancelLabel, color = MaterialTheme.colorScheme.secondary)
        }
        Button(
            onClick = onSave,
            modifier = Modifier.weight(1f)
        ) {
            Text(saveLabel)
        }
    }
}