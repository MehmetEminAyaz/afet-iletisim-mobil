package com.example.bitirmev2

data class HelpMessageRequest(
    val type: String, // ARAMA, SAGLIK, MALZEME, HAYATTAYIM
    val description: String?,
    val timestamp: Long,
    val city: String?,
    val district: String?,
    val neighborhood: String?,
    val street: String?,
    val buildingNumber: String?,
    val personCount: Int?,
    val foodNeeded: Boolean?,
    val waterNeeded: Boolean?,
    val shelterNeeded: Boolean?,
    val healthNeeded: Boolean?,
    val latitude: Double?,
    val longitude: Double?,
    val healthType: String?, // Sağlık durumu tipi (sadece sağlık yardımında)
    val victimNames: List<String>?, // Arama kurtarma için kişi isimleri
    val emergencyContactEmails: List<String>?, // Hayattayım mesajı için acil kişi mailleri
    val aidQuantities: Map<String, Int>? // Malzeme yardımı için ihtiyaçlar
)
