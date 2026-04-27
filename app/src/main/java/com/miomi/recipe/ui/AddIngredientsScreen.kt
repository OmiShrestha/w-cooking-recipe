package com.miomi.recipe.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.miomi.recipe.viewmodel.AddRecipeViewModel
import com.miomi.recipe.viewmodel.IngredientEntry

@Composable
fun AddIngredientsScreen(navController: NavController, viewModel: AddRecipeViewModel){
    val focusManager = LocalFocusManager.current
    var errorMessage by rememberSaveable { mutableStateOf("") }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                },
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                if (viewModel.isEditing) "Edit Recipe — Ingredients" else "Step 2 of 3 — Ingredients",
                style = MaterialTheme.typography.headlineLarge
            )
            HorizontalDivider(thickness = 2.dp)

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(
                    items = viewModel.formState.ingredients,
                    key = { index, _ -> index }
                ) { index, ingredient ->
                    IngredientRow(
                        ingredient = ingredient,
                        index = index,
                        showDelete = viewModel.formState.ingredients.size > 1,
                        onUpdate = { viewModel.updateIngredient(index, it) },
                        onDelete = { viewModel.removeIngredient(index) }
                    )
                    HorizontalDivider()
                }
            }

            OutlinedButton(
                onClick = { viewModel.addIngredient() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add button")
                Text(" Add Ingredient")
            }

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            FormButtons(
                onCancel = { navController.popBackStack() },
                cancelLabel = "Back",
                onSave = {
                    if (viewModel.isIngredientsValid()) {
                        navController.navigate(if (viewModel.isEditing) "edit_add_steps" else "add_steps")
                    } else {
                        errorMessage = "Please fill in all ingredient fields"
                    }
                },
                saveLabel = "Next"
            )
        }
    }
}

@Composable
private fun IngredientRow(
    ingredient: IngredientEntry,
    index: Int,
    showDelete: Boolean,
    onUpdate: (IngredientEntry) -> Unit,
    onDelete: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                "Ingredient ${index + 1}",
                style = MaterialTheme.typography.labelMedium
            )
            if (showDelete) {
                IconButton(onClick = onDelete){
                    Icon(Icons.Default.Delete, contentDescription = "delete button")
                }
            }
        }
        OutlinedTextField(
            value = ingredient.name,
            onValueChange = { onUpdate(ingredient.copy(name = it)) },
            label = {Text("Ingredient Name")},
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            OutlinedTextField(
                value = ingredient.quantity,
                onValueChange = {
                    if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                        onUpdate(ingredient.copy(quantity = it))
                    }
                },
                label = { Text("Quantity") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            OutlinedTextField(
                value = ingredient.unit,
                onValueChange = { onUpdate(ingredient.copy(unit = it)) },
                label = {Text("Unit")},
                modifier = Modifier.weight(1f)
            )
        }
    }
}