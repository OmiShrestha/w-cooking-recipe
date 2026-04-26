package com.miomi.recipe.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.miomi.recipe.model.Step
import kotlinx.coroutines.flow.Flow

@Dao
interface StepDao {

    // returns all steps for a given recipe, ordered by sequence number
    @Query("SELECT * FROM Step WHERE recipeId = :recipeId ORDER BY sequenceNum ASC")
    fun getStepsForRecipe(recipeId: Int): Flow<List<Step>>

    // returns a single step by its ID
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStep(step: Step)

    // deletes a single step
    @Delete
    suspend fun deleteStep(step: Step)

    // deletes all steps for a given recipe
    @Query("DELETE FROM Step WHERE recipeId = :recipeId")
    suspend fun deleteAllStepsForRecipe(recipeId: Int)
}
