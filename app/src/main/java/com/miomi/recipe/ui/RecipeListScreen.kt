package com.miomi.recipe.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.miomi.recipe.model.Recipe
import com.miomi.recipe.viewmodel.RecipeViewModel

@Composable
fun RecipeListScreen(navController: NavController, viewModel: RecipeViewModel) {
    val recipes by viewModel.recipesFlow.collectAsStateWithLifecycle(emptyList())

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
            ScreenTitle(onFavoritesClick = { navController.navigate("favorites") })

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                viewModel.getCategories().forEach { category ->
                    val categoryRecipes = recipes.filter {
                        it.category.equals(category, ignoreCase = true)
                    }
                    recipeCategorySection(
                        category = category,
                        recipes = categoryRecipes,
                        navController = navController,
                        onToggleFavorite = { recipe ->
                            viewModel.toggleFavorite(recipe.recipeId, recipe.isFavorite)
                        }
                    )
                }
            }
        }
    }
}

// Placeholder screen for favorites (later will be replaced with actual implementation)
@Composable
private fun ScreenTitle(onFavoritesClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Recipe List",
            style = MaterialTheme.typography.headlineLarge
        )
        IconButton(onClick = onFavoritesClick) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "View Favorites",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
    HorizontalDivider(
        modifier = Modifier.padding(top = 8.dp),
        thickness = 2.dp
    )
}

private fun LazyListScope.recipeCategorySection(
    category: String,
    recipes: List<Recipe>,
    navController: NavController,
    onToggleFavorite: (Recipe) -> Unit
) {
    item {
        Text(
            text = category.uppercase(),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
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
                onClick = { navController.navigate("recipe_detail/${recipe.recipeId}") },
                onToggleFavorite = { onToggleFavorite(recipe) }
            )
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
internal fun RecipeCard(
    recipe: Recipe,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
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
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = if (recipe.isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = if (recipe.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                )
            }
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
            text = recipe.category,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 2
        )
    }
}
