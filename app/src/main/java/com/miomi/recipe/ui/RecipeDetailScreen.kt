package com.miomi.recipe.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.miomi.recipe.model.Ingredient
import com.miomi.recipe.model.Recipe
import com.miomi.recipe.model.Step
import com.miomi.recipe.viewmodel.RecipeViewModel

@Composable
fun RecipeDetailScreen(
    navController: NavController,
    viewModel: RecipeViewModel,
    recipeId: Int?
) {
    val id = recipeId ?: -1
    val recipe by viewModel.getRecipeStream(id).collectAsStateWithLifecycle(null)
    val ingredients by viewModel.getIngredientsStream(id).collectAsStateWithLifecycle(emptyList())
    val steps by viewModel.getStepsStream(id).collectAsStateWithLifecycle(emptyList())

    if (recipe == null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text("Recipe not found")
        }
        return
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ScreenTitle()
            RecipeInfo(recipe!!, ingredients, steps)
            BackButton(navController)
        }
    }
}

@Composable
private fun ScreenTitle() {
    Text("Recipe Details", style = MaterialTheme.typography.headlineLarge)
    HorizontalDivider(modifier = Modifier.padding(bottom = 4.dp), thickness = 2.dp)
}

// Displays the recipe name, category, ingredients, and instructions
@Composable
private fun RecipeInfo(recipe: Recipe, ingredients: List<Ingredient>, steps: List<Step>) {
    Text(recipe.name, style = MaterialTheme.typography.headlineLarge)
    DetailSection(label = "Category", value = recipe.category)

    SectionHeader("INGREDIENTS")
    if (ingredients.isEmpty()) {
        Text("No ingredients added", style = MaterialTheme.typography.bodyMedium)
    } else {
        ingredients.forEach { ingredient ->
            Text(
                text = "• ${ingredient.name}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 2.dp)
            )
        }
    }
    HorizontalDivider()

    SectionHeader("INSTRUCTIONS")
    if (steps.isEmpty()) {
        Text("No instructions added", style = MaterialTheme.typography.bodyMedium)
    } else {
        steps.forEach { step ->
            Text(
                text = "${step.sequenceNum}. ${step.step}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
    HorizontalDivider()
}

// Reusable composable for displaying a labeled section
@Composable
private fun SectionHeader(label: String) {
    Text(
        text = label,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
    )
}

@Composable
fun DetailSection(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label.uppercase(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(text = value, fontSize = 15.sp)
        HorizontalDivider()
    }
}

@Composable
private fun BackButton(navController: NavController) {
    OutlinedButton(
        onClick = { navController.popBackStack() },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Back")
    }
}