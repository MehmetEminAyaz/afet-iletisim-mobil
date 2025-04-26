package com.example.bitirmev2.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.bitirmev2.data.StringListConverter
import com.example.bitirmev2.HelpMessageRequest
import java.util.UUID

@Entity
@TypeConverters(StringListConverter::class) // ✅ Room'un List<String> desteği için
data class HelpMessage(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val type: MessageType,
    val name: String,
    val address: String,
    val personCount: Int,
    val extraNote: String,
    val timestamp: Long = System.currentTimeMillis(),
    val senderName: String,

    // 📍 Yeni eklenen alanlar
    val city: String,
    val district: String,
    val neighborhood: String,
    val street: String,
    val buildingNumber: String,
    val healthType: String? = null,
    val emergencyContactEmails: List<String>? = null
)

fun HelpMessage.toRequest(): HelpMessageRequest {
    return HelpMessageRequest(
        type = when (this.type) {
            MessageType.RESCUE -> "ARAMA"
            MessageType.MEDICAL -> "SAGLIK"
            MessageType.SUPPLY -> "MALZEME"
            MessageType.ALIVE -> "HAYATTAYIM"
            MessageType.HEALTH -> "SAGLIK"
        },
        description = extraNote,
        timestamp = timestamp,
        city = city,
        district = district,
        neighborhood = neighborhood,
        street = street,
        buildingNumber = buildingNumber,
        personCount = personCount,
        foodNeeded = null,
        waterNeeded = null,
        shelterNeeded = null,
        healthNeeded = null,
        latitude = null,
        longitude = null,
        healthType = healthType,
        victimNames =  if (this.type == MessageType.RESCUE) this.name.split(",").map { it.trim() } else null,
        emergencyContactEmails = emergencyContactEmails, // ✅ Artık doğru gönderiyoruz!
        aidQuantities = null
    )
}
