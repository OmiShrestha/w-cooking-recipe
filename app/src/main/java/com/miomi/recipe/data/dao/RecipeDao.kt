package com.miomi.recipe.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.miomi.recipe.model.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipe")
    fun getAllRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipe WHERE isFavorite = 1")
    fun getFavoriteRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipe WHERE recipeId = :id")
    fun getRecipeById(id: Int): Recipe

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: Recipe)

    @Query("UPDATE recipe SET isFavorite = :isFavorite WHERE recipeId = :id")
    suspend fun updateIsFavorite(id: Int, isFavorite: Boolean)

    @Update
    suspend fun updateRecipe(recipe: Recipe)

    @Delete
    suspend fun deleteRecipe(recipe: Recipe)

}