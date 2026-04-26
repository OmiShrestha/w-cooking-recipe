package com.miomi.recipe.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.miomi.recipe.model.Ingredient
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDao {
    @Query("SELECT * FROM Ingredient WHERE recipeId = :recipeId")           // adding recipeId to the query to filter ingredients by recipe
    fun getAllIngredientsForRecipe(recipeId: Int): Flow<List<Ingredient>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredient(ingredient: Ingredient)

    @Delete
    suspend fun deleteIngredient(ingredient: Ingredient)

    @Query("DELETE FROM Ingredient WHERE recipeId = :recipeId")             // adding recipeId to the query to delete ingredients by recipe 
    suspend fun deleteAllIngredientsForRecipe(recipeId: Int)
}
