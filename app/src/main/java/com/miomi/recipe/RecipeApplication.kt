package com.miomi.recipe

import android.app.Application
import com.miomi.recipe.data.RecipeDatabase
import com.miomi.recipe.data.repository.RecipeRepository
import com.miomi.recipe.data.repository.RecipeRepositoryImpl

class RecipeApplication : Application() {
    lateinit var recipeRepository: RecipeRepository
    override fun onCreate() {
        super.onCreate()
        recipeRepository =
            RecipeRepositoryImpl(RecipeDatabase.getDatabase(this))
    }
}