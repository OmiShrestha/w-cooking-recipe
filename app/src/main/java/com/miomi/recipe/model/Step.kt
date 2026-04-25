package com.miomi.recipe.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Step")
data class Step(
    @PrimaryKey
    val stepId: Int,
    val sequenceNum: Int,
    val step: String
)
