package com.example.bitirmev2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1, // sabit 1 kayıt olacak
    val name: String,
    val surname: String,
    val city: String,
    val district: String,
    val neighborhood: String,
    val street: String,
    val apartmentNo: String
)
