package com.example.bitirmev2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "next_of_kin")
data class NextOfKin(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val order: Int // 1, 2, 3 sıralaması
)
