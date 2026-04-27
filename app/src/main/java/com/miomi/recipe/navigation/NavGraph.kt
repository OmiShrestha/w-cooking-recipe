package com.miomi.recipe.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.miomi.recipe.viewmodel.IngredientEntry
import com.miomi.recipe.viewmodel.RecipeViewModel
import com.miomi.recipe.viewmodel.StepEntry
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

        // nested navigation graph for editing a recipe, reusing the same screens as the create flow but with pre-populated data
        navigation(
            startDestination = "edit_recipe_details/{recipeId}",
            route = "edit_recipe_flow/{recipeId}"
        ) {
            composable("edit_recipe_details/{recipeId}") { entry ->
                val parentEntry = remember(entry) {
                    navController.getBackStackEntry("edit_recipe_flow/{recipeId}")
                }
                val addRecipeViewModel: AddRecipeViewModel = viewModel(parentEntry)
                val recipeId = entry.arguments?.getString("recipeId")?.toInt()

                if (recipeId != null) {
                    val recipeWithDetails by recipeViewModel
                        .getRecipeWithDetails(recipeId)
                        .collectAsStateWithLifecycle(null)

                    LaunchedEffect(recipeWithDetails) {
                        recipeWithDetails?.let { details ->
                            if (!addRecipeViewModel.isEditing) {
                                addRecipeViewModel.loadExisting(
                                    recipeId = details.recipe.recipeId,
                                    name = details.recipe.name,
                                    category = details.recipe.category,
                                    isFavorite = details.recipe.isFavorite,
                                    ingredients = details.ingredients.map { ing ->
                                        IngredientEntry(
                                            name = ing.name,
                                            quantity = if (ing.quantity % 1.0 == 0.0)
                                                ing.quantity.toInt().toString()
                                            else
                                                ing.quantity.toString(),
                                            unit = ing.unit
                                        )
                                    },
                                    steps = details.steps.map { s -> StepEntry(description = s.step) }
                                )
                            }
                        }
                    }
                }

                AddRecipeDetailsScreen(navController, addRecipeViewModel)
            }

            composable("edit_add_ingredients") { entry ->
                val parentEntry = remember(entry) {
                    navController.getBackStackEntry("edit_recipe_flow/{recipeId}")
                }
                val addRecipeViewModel: AddRecipeViewModel = viewModel(parentEntry)
                AddIngredientsScreen(navController, addRecipeViewModel)
            }

            composable("edit_add_steps") { entry ->
                val parentEntry = remember(entry) {
                    navController.getBackStackEntry("edit_recipe_flow/{recipeId}")
                }
                val addRecipeViewModel: AddRecipeViewModel = viewModel(parentEntry)
                AddStepsScreen(navController, addRecipeViewModel, recipeViewModel)
            }
        }

        // placeholder for favorites screen
        composable("favorites") {
            FavoritesScreen(navController, recipeViewModel)
        }
    }
}