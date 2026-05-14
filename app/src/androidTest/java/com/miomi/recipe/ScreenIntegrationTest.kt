// Author: Omi Shrestha + mike romano

package com.miomi.recipe

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.miomi.recipe.model.Recipe
import com.miomi.recipe.ui.AddIngredientsScreen
import com.miomi.recipe.ui.AddRecipeDetailsScreen
import com.miomi.recipe.ui.AddStepsScreen
import com.miomi.recipe.ui.RecipeCard
import com.miomi.recipe.ui.RecipeDetailScreen
import com.miomi.recipe.ui.RecipeListScreen
import com.miomi.recipe.viewmodel.AddRecipeViewModel
import com.miomi.recipe.viewmodel.RecipeViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScreenIntegrationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var fakeRepository: FakeRecipeRepository
    private lateinit var recipeViewModel: RecipeViewModel

    @Before
    fun setup() {
        fakeRepository = FakeRecipeRepository()
        recipeViewModel = RecipeViewModel(fakeRepository)
    }

    // Verifies that RecipeCard renders the recipe's name and category, and that
    // the card and favorite button are clickable with the correct favorite state
    @Test
    fun recipeCardDisplaysNameAndCategory() {
        val recipe = Recipe(recipeId = 1, name = "Pasta", category = "Dinner", isFavorite = false)

        composeTestRule.setContent {
            RecipeCard(recipe = recipe, onClick = {}, onToggleFavorite = {})
        }

        composeTestRule.onNodeWithText("Pasta").assertIsDisplayed()
        composeTestRule.onNodeWithText("Dinner").assertIsDisplayed()
        composeTestRule.onNodeWithText("Pasta").assertHasClickAction()
        composeTestRule.onNodeWithContentDescription("Add to favorites").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Add to favorites").assertHasClickAction()
    }

    // Verifies that RecipeCard shows "Remove from favorites" when isFavorite is true
    @Test
    fun recipeCardShowsRemoveFromFavoritesWhenFavorite() {
        val recipe = Recipe(recipeId = 2, name = "Pizza", category = "Lunch", isFavorite = true)

        composeTestRule.setContent {
            RecipeCard(recipe = recipe, onClick = {}, onToggleFavorite = {})
        }

        composeTestRule.onNodeWithContentDescription("Remove from favorites").assertIsDisplayed()
    }

    // Verifies that tapping Next with blank fields shows the validation error message
    @Test
    fun addRecipeDetailsScreenShowsErrorWhenFieldsBlank() {
        composeTestRule.setContent {
            AddRecipeDetailsScreen(
                navController = rememberNavController(),
                viewModel = AddRecipeViewModel()
            )
        }

        composeTestRule.onNodeWithText("Next").performClick()

        composeTestRule.onNodeWithText("Please fill in all fields").assertIsDisplayed()
    }

    //verifies that the admin sees the add recipe button
    @Test
    fun adminSeesAddRecipeButton() {
        composeTestRule.setContent {
            RecipeListScreen(
                navController = rememberNavController(),
                viewModel = recipeViewModel,
                isAdmin = true,
                onSignOut = {}
            )
        }
        composeTestRule.onNodeWithContentDescription("add_recipe").assertIsDisplayed()
    }

    //verifies that non-admins do not see the recipe button
    @Test
    fun nonAdminDoesNotSeeAddRecipeButton() {
        composeTestRule.setContent {
            RecipeListScreen(
                navController = rememberNavController(),
                viewModel = recipeViewModel,
                isAdmin = false,
                onSignOut = {}
            )
        }
        composeTestRule.onNodeWithContentDescription("add_recipe").assertDoesNotExist()
    }

    @Test
    fun addIngredientsScreenShowsErrorWhenFieldsBlank() {
        composeTestRule.setContent {
            AddIngredientsScreen(
                navController = rememberNavController(),
                viewModel = AddRecipeViewModel()
            )
        }

        composeTestRule.onNodeWithText("Next").performClick()

        composeTestRule.onNodeWithText("Please fill in all ingredient fields").assertIsDisplayed()
    }

    //verifies that clicking add ingredient button adds a new row to the list
    @Test
    fun addIngredientsScreenAddsIngredientRow() {
        composeTestRule.setContent {
            AddIngredientsScreen(
                navController = rememberNavController(),
                viewModel = AddRecipeViewModel()
            )
        }

        composeTestRule.onNodeWithText("Ingredient 1").assertIsDisplayed()
        composeTestRule.onNodeWithText(" Add Ingredient").performClick()
        composeTestRule.onNodeWithText("Ingredient 2").assertIsDisplayed()
    }

    @Test
    fun addStepsScreenShowsErrorWhenFieldsBlank() {
        composeTestRule.setContent {
            AddStepsScreen(
                navController = rememberNavController(),
                addRecipeViewModel = AddRecipeViewModel(),
                recipeViewModel = recipeViewModel
            )
        }

        composeTestRule.onNodeWithText("Save Recipe").performClick()

        composeTestRule.onNodeWithText("Please fill in all step fields").assertIsDisplayed()
    }

    @Test
    fun addStepsScreenAddsStepRow() {
        composeTestRule.setContent {
            AddStepsScreen(
                navController = rememberNavController(),
                addRecipeViewModel = AddRecipeViewModel(),
                recipeViewModel = recipeViewModel
            )
        }

        composeTestRule.onNodeWithText("Step 1").assertIsDisplayed()
        composeTestRule.onNodeWithText(" Add Step").performClick()
        composeTestRule.onNodeWithText("Step 2").assertIsDisplayed()
    }




    // only admin should see the edit recipe button
    @Test
    fun adminSeesEditRecipeButton() {
        val recipe = Recipe(recipeId = 1, name = "Dr. Bi's famous meatloaf", category = "Dinner", isFavorite = false)
        fakeRepository.seedRecipes(recipe)

        composeTestRule.setContent {
            RecipeDetailScreen(
                navController = rememberNavController(),
                viewModel = recipeViewModel,
                recipeId = 1,
                isAdmin = true
            )
        }

        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Edit Recipe").assertIsDisplayed()
    }

    // non admins should not see the edit recipe button
    @Test
    fun nonAdminDoesNotSeeEditRecipeButton() {
        val recipe = Recipe(recipeId = 1, name = "Dr. Bi's favorite sandwich", category = "Dinner", isFavorite = false)
        fakeRepository.seedRecipes(recipe)

        composeTestRule.setContent {
            RecipeDetailScreen(
                navController = rememberNavController(),
                viewModel = recipeViewModel,
                recipeId = 1,
                isAdmin = false
            )
        }

        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Edit Recipe").assertDoesNotExist()
    }


}
