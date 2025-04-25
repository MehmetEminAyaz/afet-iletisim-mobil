package com.example.bitirmev2.data

object AddressDataProvider {
    val addressData = mapOf(
        "Ankara" to mapOf(
            "Çankaya" to mapOf(
                "Bahçelievler Mahallesi" to mapOf(
                    "7. Cadde" to listOf("1", "2", "3"),
                    "Aşkaabat Caddesi" to listOf("12", "14", "16"),
                    "Kazım Karabekir Sokak" to listOf("7", "8", "9")
                ),
                "Emek Mahallesi" to mapOf(
                    "Gazi Caddesi" to listOf("10", "11", "12"),
                    "Beşevler Sokak" to listOf("3", "5", "7"),
                    "Turgut Özal Sokak" to listOf("15", "17", "19")
                ),
                "Kızılay Mahallesi" to mapOf(
                    "Atatürk Bulvarı" to listOf("101", "103", "105"),
                    "Mithatpaşa Caddesi" to listOf("200", "202", "204"),
                    "Sakarya Caddesi" to listOf("30", "32", "34")
                )
            ),
            "Yenimahalle" to mapOf(
                "Demetevler Mahallesi" to mapOf(
                    "Karanfil Sokak" to listOf("1", "2", "3"),
                    "Zambak Sokak" to listOf("4", "5", "6"),
                    "Orkide Caddesi" to listOf("7", "8", "9")
                ),
                "Batı Sitesi Mahallesi" to mapOf(
                    "İsmet İnönü Caddesi" to listOf("10", "11", "12"),
                    "Papatya Sokak" to listOf("13", "14", "15"),
                    "Lale Sokak" to listOf("16", "17", "18")
                ),
                "Ostim Mahallesi" to mapOf(
                    "Sanayi Caddesi" to listOf("20", "21", "22"),
                    "Atölye Sokak" to listOf("23", "24", "25"),
                    "Dükkan Sokak" to listOf("26", "27", "28")
                )
            ),
            "Mamak" to mapOf(
                "Abidinpaşa Mahallesi" to mapOf(
                    "Mehmet Akif Caddesi" to listOf("1", "2", "3"),
                    "Umut Sokak" to listOf("4", "5", "6"),
                    "Yenilik Caddesi" to listOf("7", "8", "9")
                ),
                "Durali Alıç Mahallesi" to mapOf(
                    "Köy Yolu Caddesi" to listOf("10", "11", "12"),
                    "Tarla Sokak" to listOf("13", "14", "15"),
                    "İstiklal Sokak" to listOf("16", "17", "18")
                ),
                "Kutlu Mahallesi" to mapOf(
                    "Park Caddesi" to listOf("20", "21", "22"),
                    "Dere Sokak" to listOf("23", "24", "25"),
                    "Yokuş Sokak" to listOf("26", "27", "28")
                )
            )
        )
    )

    fun getDistricts(city: String): List<String> =
        addressData[city]?.keys?.toList() ?: emptyList()

    fun getNeighborhoods(city: String, district: String): List<String> =
        addressData[city]?.get(district)?.keys?.toList() ?: emptyList()

    fun getStreets(city: String, district: String, neighborhood: String): List<String> =
        addressData[city]?.get(district)?.get(neighborhood)?.keys?.toList() ?: emptyList()

    fun getApartments(city: String, district: String, neighborhood: String, street: String): List<String> =
        addressData[city]?.get(district)?.get(neighborhood)?.get(street) ?: emptyList()
}
