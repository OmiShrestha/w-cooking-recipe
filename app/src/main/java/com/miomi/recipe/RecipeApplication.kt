package com.miomi.recipe

import android.app.Application
import com.miomi.recipe.data.RecipeDatabase
import com.miomi.recipe.data.repository.RecipeRepository
import com.miomi.recipe.data.repository.RecipeRepositoryImpl
import com.miomi.recipe.network.RetrofitClient
import com.miomi.recipe.network.repository.MealRepository
import com.miomi.recipe.network.repository.MealRepositoryImpl

class RecipeApplication : Application() {
    lateinit var recipeRepository: RecipeRepository
    lateinit var mealRepository: MealRepository

    override fun onCreate() {
        super.onCreate()
        recipeRepository = RecipeRepositoryImpl(RecipeDatabase.getDatabase(this))
        mealRepository = MealRepositoryImpl(RetrofitClient.mealApiService)
    }
}