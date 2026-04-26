package com.miomi.recipe.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Step",
    foreignKeys = [ForeignKey(
        entity = Recipe::class,
        parentColumns = ["recipeId"],
        childColumns = ["recipeId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("recipeId")]
)
data class Step(
    @PrimaryKey(autoGenerate = true) val stepId: Int = 0,
    val recipeId: Int,    //Foreign key in recipe table
    val sequenceNum: Int,
    val step: String
)
