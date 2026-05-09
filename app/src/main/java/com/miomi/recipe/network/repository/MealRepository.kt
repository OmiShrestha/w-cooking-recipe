package com.miomi.recipe.network.repository

import com.miomi.recipe.network.model.MealDto

interface MealRepository {
    suspend fun searchByName(name: String): Result<List<MealDto>>
    suspend fun filterByCategory(category: String): Result<List<MealDto>>
    suspend fun filterByIngredient(ingredient: String): Result<List<MealDto>>
    suspend fun lookupById(id: String): Result<MealDto?>
}
