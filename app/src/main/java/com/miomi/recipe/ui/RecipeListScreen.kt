package com.miomi.recipe.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.miomi.recipe.model.Recipe
import com.miomi.recipe.viewmodel.RecipeViewModel
import kotlin.collections.forEach
import kotlin.text.uppercase
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlin.collections.filter
import kotlin.collections.forEach
import kotlin.text.equals
import kotlin.text.uppercase

@Composable
fun RecipeListScreen(navController: NavController, viewModel: RecipeViewModel) {
    val recipes by viewModel.recipes.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_recipe") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "add_recipe")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            ScreenTitle()

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                viewModel.getCategories().forEach { category ->
                    val categoryRecipes = recipes.filter {
                        it.category.equals(category, ignoreCase = true)
                    }
                    recipeCategorySection(category, categoryRecipes, navController)
                }
            }
        }
    }
}

@Composable
private fun ScreenTitle(){
    Text(
        text = "Recipe List",
        style = MaterialTheme.typography.headlineLarge
    )

    HorizontalDivider(
        modifier = Modifier.padding(top = 8.dp),
        thickness = 2.dp
    )
}

private fun LazyListScope.recipeCategorySection(
    category: String,
    recipes: List<Recipe>,
    navController: NavController
) {
    item {
        Text(text = category.uppercase(),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
    }
    if (recipes.isEmpty()) {
        item {
            Text(
                text = "Click the plus to add a $category recipe",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    } else {
        items(recipes) { recipe ->
            RecipeCard(
                recipe = recipe,
                onClick = { navController.navigate("recipe_detail/${recipe.id}") })
        }
        item {
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun RecipeCard(recipe: Recipe,
               onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            CardAccent()

            CardContent(recipe)

            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 12.dp)
            )
        }

    }
}

@Composable
private fun CardAccent() {
    Box(
        modifier = Modifier
            .width(4.dp)
            .height(80.dp)
            .background(MaterialTheme.colorScheme.primary)
    )
}

@Composable
private fun RowScope.CardContent(recipe: Recipe) {
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(12.dp)
    ) {
        Text(
            text = recipe.name,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = recipe.ingredients,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 2
        )
    }
}
