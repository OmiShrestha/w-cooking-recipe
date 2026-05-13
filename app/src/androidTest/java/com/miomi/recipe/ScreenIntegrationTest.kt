// Author: Omi Shrestha

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
import com.miomi.recipe.ui.AddRecipeDetailsScreen
import com.miomi.recipe.ui.RecipeCard
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
}
