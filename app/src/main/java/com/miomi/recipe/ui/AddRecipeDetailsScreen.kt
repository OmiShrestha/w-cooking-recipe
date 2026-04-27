package com.miomi.recipe.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.miomi.recipe.viewmodel.AddRecipeViewModel

@Composable
fun AddRecipeDetailsScreen(navController: NavController, viewModel: AddRecipeViewModel) {
    val focusManager = LocalFocusManager.current
    var errorMessage by rememberSaveable { mutableStateOf("") }

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
            Text(
                if (viewModel.isEditing) "Edit Recipe — Details" else "Step 1 of 3 — Details",
                style = MaterialTheme.typography.headlineLarge
            )
            HorizontalDivider(thickness = 2.dp)

            RecipeDetailsSection(
                name = viewModel.formState.recipeName,
                category = viewModel.formState.category,
                focusManager = focusManager,
                categories = viewModel.getCategories(),
                onNameChange = { viewModel.updateRecipeName(it) },
                onCategoryChange = { viewModel.updateCategory(it) }
            )

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            FormButtons(
                onCancel = { navController.popBackStack() },
                onSave = {
                    if (viewModel.isDetailsValid()) {
                        navController.navigate(if (viewModel.isEditing) "edit_add_ingredients" else "add_ingredients")
                    } else {
                        errorMessage = "Please fill in all fields"
                    }
                },
                saveLabel = "Next"
            )
        }
    }
}
