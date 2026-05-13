package com.miomi.recipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.miomi.recipe.navigation.NavGraph
import com.miomi.recipe.ui.theme.RecipeTheme
import com.miomi.recipe.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {

    // Initialize the AuthViewModel
    private val authViewModel: AuthViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val app = application as RecipeApplication
                @Suppress("UNCHECKED_CAST")
                return AuthViewModel(app.authRepositoryImpl) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipeTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController, authViewModel = authViewModel)
            }
        }
    }
}