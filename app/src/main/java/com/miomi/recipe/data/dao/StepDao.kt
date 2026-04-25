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
    @Query("SELECT * FROM step WHERE stepId = :recipeId ORDER BY sequenceNum ASC")
    fun getStepsForRecipe(recipeId: Int): Flow<List<Step>>


    @Query("DELETE FROM step WHERE stepId = :recipeId")
    suspend fun deleteAllStepsForRecipe(recipeId: Int)

    @Delete
    suspend fun deleteStep(step: Step)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStep(step: Step)


}