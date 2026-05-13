// Author: Omi Shrestha

package com.miomi.recipe

import com.miomi.recipe.network.model.MealDto
import com.miomi.recipe.network.repository.MealRepository

// A fake implementation of MealRepository for testing purposes
class FakeMealRepository : MealRepository {

    var searchByNameResult: Result<List<MealDto>> = Result.success(emptyList())
    var filterByCategoryResult: Result<List<MealDto>> = Result.success(emptyList())
    var filterByIngredientResult: Result<List<MealDto>> = Result.success(emptyList())
    var lookupByIdResult: Result<MealDto?> = Result.success(null)

    var lastSearchByNameQuery: String? = null
    var lastFilterByCategoryQuery: String? = null
    var lastFilterByIngredientQuery: String? = null

    override suspend fun searchByName(name: String): Result<List<MealDto>> {
        lastSearchByNameQuery = name
        return searchByNameResult
    }

    override suspend fun filterByCategory(category: String): Result<List<MealDto>> {
        lastFilterByCategoryQuery = category
        return filterByCategoryResult
    }

    override suspend fun filterByIngredient(ingredient: String): Result<List<MealDto>> {
        lastFilterByIngredientQuery = ingredient
        return filterByIngredientResult
    }

    override suspend fun lookupById(id: String): Result<MealDto?> {
        return lookupByIdResult
    }
}
