package com.miomi.recipe.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.miomi.recipe.viewmodel.AddRecipeViewModel
import com.miomi.recipe.viewmodel.RecipeViewModel
import com.miomi.recipe.viewmodel.StepEntry

@Composable
fun AddStepsScreen(
    navController: NavController,
    addRecipeViewModel: AddRecipeViewModel,
    recipeViewModel: RecipeViewModel
){
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
            Text("Add Recipe", style = MaterialTheme.typography.headlineLarge)
            HorizontalDivider(thickness = 2.dp)

            Text(
                "Step 3 of 3 — Steps",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary
            )

            addRecipeViewModel.formState.steps.forEachIndexed {index, step ->
                StepRow(
                    step = step,
                    index = index,
                    showDelete = addRecipeViewModel.formState.steps.size > 1,
                    onUpdate = { addRecipeViewModel.updateStep(index, it) },
                    onDelete = { addRecipeViewModel.removeStep(index) }
                )
                HorizontalDivider()
            }

            OutlinedButton(
                onClick = {addRecipeViewModel.addStep() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add step")
                Text(" Add Step")
            }

            if (errorMessage.isNotEmpty()){
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Back", color = MaterialTheme.colorScheme.secondary)
                }
                Button(
                    onClick = {
                        if (addRecipeViewModel.isStepsValid()) {
                            recipeViewModel.newAddRecipe(addRecipeViewModel.formState)
                            addRecipeViewModel.clearForm()
                            navController.popBackStack("recipe_list", inclusive = false)
                        } else {
                            errorMessage = "Please fill in all step fields"
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Save Recipe")
                }
            }
        }
    }
}


@Composable
private fun StepRow(
    step: StepEntry,
    index: Int,
    showDelete: Boolean,
    onUpdate: (StepEntry) -> Unit,
    onDelete: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Step ${index + 1}",
                style = MaterialTheme.typography.labelMedium
            )
            if (showDelete) {
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Step Button")
                }
            }
        }
        OutlinedTextField(
            value = step.description,
            onValueChange = { onUpdate(step.copy(description = it)) },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )
    }
}