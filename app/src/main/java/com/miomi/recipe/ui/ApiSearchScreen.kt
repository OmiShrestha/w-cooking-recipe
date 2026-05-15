package com.miomi.recipe.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.miomi.recipe.network.model.MealDto
import com.miomi.recipe.viewmodel.ApiSearchViewModel
import com.miomi.recipe.viewmodel.SearchType

// Composable screen for searching recipes from TheMealDB API
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiSearchScreen(navController: NavController, viewModel: ApiSearchViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Recipes Online") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            SearchTypeSelector(
                selectedType = uiState.searchType,
                onTypeSelected = viewModel::onSearchTypeChanged
            )
            Spacer(modifier = Modifier.height(8.dp))
            SearchInputRow(
                query = uiState.query,
                onQueryChanged = viewModel::onQueryChanged,
                onSearch = viewModel::search,
                placeholder = uiState.searchType.placeholder
            )
            Spacer(modifier = Modifier.height(12.dp))
            when {
                uiState.isLoading -> LoadingIndicator()
                uiState.error != null -> ErrorMessage(uiState.error!!)
                uiState.hasSearched && uiState.results.isEmpty() -> EmptyResults()
                uiState.results.isNotEmpty() -> MealResultsList(
                    meals = uiState.results,
                    onMealClick = { meal -> navController.navigate("api_meal_detail/${meal.idMeal}") }
                )
                else -> {}
            }
        }
    }
}

// Composable screen for typing search queries and selecting search type (name, category, ingredient)
@Composable
private fun SearchTypeSelector(selectedType: SearchType, onTypeSelected: (SearchType) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        SearchType.entries.forEach { type ->
            FilterChip(
                selected = type == selectedType,
                onClick = { onTypeSelected(type) },
                label = { Text(type.label) }
            )
        }
    }
}

// Composable screen for searching recipes to input search queries
@Composable
private fun SearchInputRow(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearch: () -> Unit,
    placeholder: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChanged,
            modifier = Modifier.weight(1f),
            placeholder = { Text(placeholder) },
            singleLine = true
        )
        Button(onClick = onSearch) {
            Icon(Icons.Default.Search, contentDescription = "Search")
        }
    }
}

// Composable screen to display meal results
@Composable
private fun MealResultsList(meals: List<MealDto>, onMealClick: (MealDto) -> Unit) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(meals, key = { it.idMeal }) { meal ->
            MealResultCard(meal = meal, onClick = { onMealClick(meal) })
        }
    }
}

// Composable screen to display meal details fetched
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MealResultCard(meal: MealDto, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Use Coil's AsyncImage to load meal thumbnail from URL
            AsyncImage(
                model = meal.strMealThumb,
                contentDescription = meal.strMeal,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(meal.strMeal, style = MaterialTheme.typography.titleMedium)
                if (!meal.strCategory.isNullOrBlank()) {
                    Text(
                        text = meal.strCategory,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Loading state
@Composable
private fun LoadingIndicator() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

// Error state
@Composable
private fun ErrorMessage(message: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(message, color = MaterialTheme.colorScheme.error)
    }
}

// Empty results state
@Composable
private fun EmptyResults() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("No results found", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
