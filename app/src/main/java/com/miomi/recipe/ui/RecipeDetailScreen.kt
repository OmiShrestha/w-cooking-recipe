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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.miomi.recipe.model.Recipe
import com.miomi.recipe.viewmodel.RecipeViewModel
import kotlin.let
import kotlin.text.uppercase

@Composable
fun RecipeDetailScreen(
    navController: NavController,
    viewModel: RecipeViewModel,
    recipeId: Int?
) {
    val recipe = recipeId?.let { viewModel.getRecipeById(it) }

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

            RecipeInfo(recipe)

            BackButton(navController)
        }
    }
}

@Composable
private fun ScreenTitle() {
    Text(
        "Recipe Details",
        style = MaterialTheme.typography.headlineLarge
    )

    HorizontalDivider(
        modifier = Modifier.padding(bottom = 4.dp),
        thickness = 2.dp
    )
}

@Composable
private fun RecipeInfo(recipe: Recipe) {
    Text(
        recipe.name,
        style = MaterialTheme.typography.headlineLarge
    )

    DetailSection(label = "Category", value = recipe.category)
    DetailSection(label = "Ingredients", value = recipe.ingredients)
    DetailSection(label = "Instructions", value = recipe.instructions)
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