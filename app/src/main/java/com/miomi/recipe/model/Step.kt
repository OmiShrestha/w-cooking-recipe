package com.miomi.recipe.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Step")
data class Step(
    @PrimaryKey(autoGenerate = true)
    val stepId: Int = 0,         // adding stepId as primary key for the Step table
    val recipeId: Int,           // adding recipeId to link step to its recipe
    val sequenceNum: Int,
    val step: String
)
