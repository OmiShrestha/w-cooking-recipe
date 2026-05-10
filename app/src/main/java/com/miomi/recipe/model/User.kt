package com.miomi.recipe.model
 
import androidx.room.Entity
import androidx.room.PrimaryKey

// User data class representing a user in the system
@Entity(tableName = "user")
data class User(
    @PrimaryKey val email: String,
    val name: String,
    val profilePictureUrl: String?,
    val role: UserRole
)