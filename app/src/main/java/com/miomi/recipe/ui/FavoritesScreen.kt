package com.miomi.recipe.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.miomi.recipe.viewmodel.RecipeViewModel

// Placeholder screen for favorites (later will be replaced with actual implementation)
@Composable
fun FavoritesScreen(navController: NavController, viewModel: RecipeViewModel) {
    val favorites by viewModel.favoritesFlow.collectAsStateWithLifecycle(emptyList())

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            ScreenHeader(onBack = { navController.popBackStack() })

            if (favorites.isEmpty()) {
                Text(
                    text = "No favorites yet. Tap the heart on any recipe to save it here.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 16.dp)
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(favorites) { recipe ->
                        RecipeCard(
                            recipe = recipe,
                            onClick = { navController.navigate("recipe_detail/${recipe.recipeId}") },
                            onToggleFavorite = { viewModel.toggleFavorite(recipe.recipeId, recipe.isFavorite) }
                        )
                    }
                }
            }
        }
    }
}

// Simple header with title and back button for the favorites screen
@Composable
private fun ScreenHeader(onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Favorites", style = MaterialTheme.typography.headlineLarge)
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }
    }
    HorizontalDivider(modifier = Modifier.padding(top = 8.dp), thickness = 2.dp)
}
