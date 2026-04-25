package com.miomi.recipe.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.miomi.recipe.ui.AddRecipeScreen
import com.miomi.recipe.ui.RecipeDetailScreen
import com.miomi.recipe.ui.RecipeListScreen
import com.miomi.recipe.viewmodel.RecipeViewModel
import kotlin.text.toInt

@Composable
fun NavGraph(navController: NavHostController) {
    val recipeViewModel: RecipeViewModel = viewModel(
        factory = RecipeViewModel.Factory
    )

    NavHost(
        navController = navController,
        startDestination = "recipe_list"
    ) {
        composable("recipe_list") {
            RecipeListScreen(navController, recipeViewModel)
        }

        composable("add_recipe") {
            AddRecipeScreen(navController, recipeViewModel)
        }

        composable("recipe_detail/{recipeId}") { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")?.toInt()
            RecipeDetailScreen(navController, recipeViewModel, recipeId)
        }
    }
}