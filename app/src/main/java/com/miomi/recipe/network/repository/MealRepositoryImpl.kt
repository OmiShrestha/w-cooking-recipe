package com.miomi.recipe.network.repository

import com.miomi.recipe.network.MealApiService
import com.miomi.recipe.network.model.MealDto

class MealRepositoryImpl(private val apiService: MealApiService) : MealRepository {

    override suspend fun searchByName(name: String): Result<List<MealDto>> = runCatching {
        apiService.searchByName(name).meals ?: emptyList()
    }

    override suspend fun filterByCategory(category: String): Result<List<MealDto>> = runCatching {
        apiService.filterByCategory(category).meals ?: emptyList()
    }

    override suspend fun filterByIngredient(ingredient: String): Result<List<MealDto>> = runCatching {
        apiService.filterByIngredient(ingredient).meals ?: emptyList()
    }

    override suspend fun lookupById(id: String): Result<MealDto?> = runCatching {
        apiService.lookupById(id).meals?.firstOrNull()
    }
}
