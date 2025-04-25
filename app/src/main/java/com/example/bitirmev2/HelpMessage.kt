package com.example.bitirmev2.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class HelpMessage(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val type: MessageType,
    val name: String,
    val address: String,
    val personCount: Int,
    val extraNote: String,
    val timestamp: Long = System.currentTimeMillis(),
    val senderName: String

)
