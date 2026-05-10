package com.miomi.recipe.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.miomi.recipe.network.model.MealDto
import com.miomi.recipe.viewmodel.ApiMealDetailViewModel

// Composable screen to display meal details fetched from TheMealDB API
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiMealDetailScreen(
    navController: NavController,
    mealId: String,
    viewModel: ApiMealDetailViewModel,
    isAdmin: Boolean = false
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(mealId) { viewModel.loadMeal(mealId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.meal?.strMeal ?: "Recipe Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            if (uiState.meal != null && isAdmin) {
                SaveFab(isSaved = uiState.isSaved, onSave = viewModel::saveMeal)
            }
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            uiState.error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(uiState.error!!, color = MaterialTheme.colorScheme.error)
            }
            uiState.meal != null -> MealDetailContent(uiState.meal!!, paddingValues)
        }
    }
}

// Composable to display meal details including category, area, ingredients, and instructions
@Composable
private fun SaveFab(isSaved: Boolean, onSave: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = { if (!isSaved) onSave() },
        icon = {
            Icon(
                imageVector = if (isSaved) Icons.Default.Check else Icons.Default.Add,
                contentDescription = null
            )
        },
        text = { Text(if (isSaved) "Saved" else "Save Recipe") },
        containerColor = if (isSaved) MaterialTheme.colorScheme.secondaryContainer
                         else MaterialTheme.colorScheme.primaryContainer
    )
}

// Composable to display meal details including category, area, ingredients, and instructions
@Composable
private fun MealDetailContent(meal: MealDto, paddingValues: PaddingValues) {
    val stepLabelRegex = Regex("^step\\s*\\d+\\.?$", RegexOption.IGNORE_CASE)
    val steps = meal.strInstructions
        ?.split("\r\n", "\n")
        ?.map { it.trim() }
        ?.filter { it.isNotBlank() && !it.matches(stepLabelRegex) }
        ?: emptyList()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        item { CategoryChips(meal.strCategory, meal.strArea) }
        if (meal.getIngredients().isNotEmpty()) {
            item { SectionHeader("Ingredients") }
            items(meal.getIngredients()) { (name, measure) ->
                IngredientRow(name, measure)
            }
        }
        if (steps.isNotEmpty()) {
            item { SectionHeader("Instructions") }
            itemsIndexed(steps) { index, step ->
                StepRow(index + 1, step)
            }
        }
        item { Spacer(Modifier.height(80.dp)) }
    }
}

// Composable to display category and area as chips
@Composable
private fun CategoryChips(category: String?, area: String?) {
    Row(
        modifier = Modifier.padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (!category.isNullOrBlank()) AssistChip(onClick = {}, label = { Text(category) })
        if (!area.isNullOrBlank()) AssistChip(onClick = {}, label = { Text(area) })
    }
}

// Composable to display section headers for ingredients and instructions
@Composable
private fun SectionHeader(title: String) {
    Text(title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 12.dp))
    HorizontalDivider()
}

// Composable to display an ingredient and its measure in a row
@Composable
private fun IngredientRow(name: String, measure: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(name, style = MaterialTheme.typography.bodyMedium)
        Text(measure, style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

// Composable to display a cooking step with its sequence number
@Composable
private fun StepRow(number: Int, step: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("$number.", style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary)
        Text(step, style = MaterialTheme.typography.bodyMedium)
    }
}
