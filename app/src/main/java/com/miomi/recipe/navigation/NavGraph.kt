package com.miomi.recipe.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.miomi.recipe.ui.AddIngredientsScreen
import com.miomi.recipe.ui.AddRecipeDetailsScreen
import com.miomi.recipe.ui.AddStepsScreen
import com.miomi.recipe.ui.FavoritesScreen
import com.miomi.recipe.ui.RecipeDetailScreen
import com.miomi.recipe.ui.RecipeListScreen
import com.miomi.recipe.viewmodel.AddRecipeViewModel
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

        navigation(startDestination = "add_recipe_details", route = "create_recipe_flow"){
            composable("add_recipe_details") { entry ->
                val parentEntry = remember(entry){
                    navController.getBackStackEntry("create_recipe_flow")
                }
                val addRecipeViewModel: AddRecipeViewModel = viewModel(parentEntry)
                AddRecipeDetailsScreen(navController, addRecipeViewModel)
            }

            composable("add_ingredients") { entry ->
                val parentEntry = remember(entry) {
                    navController.getBackStackEntry("create_recipe_flow")
                }
                val addRecipeViewModel: AddRecipeViewModel = viewModel(parentEntry)
                AddIngredientsScreen(navController, addRecipeViewModel)
            }

            composable("add_steps") { entry ->
                val parentEntry = remember(entry) {
                    navController.getBackStackEntry("create_recipe_flow")
                }
                val addRecipeViewModel: AddRecipeViewModel = viewModel(parentEntry)
                AddStepsScreen(navController, addRecipeViewModel, recipeViewModel)
            }
        }

        composable("recipe_detail/{recipeId}") { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")?.toInt()
            RecipeDetailScreen(navController, recipeViewModel, recipeId)
        }

        // placeholder for favorites screen
        composable("favorites") {
            FavoritesScreen(navController, recipeViewModel)
        }
    }
}