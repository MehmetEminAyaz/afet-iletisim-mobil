package com.example.bitirmev2

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bitirmev2.components.AdresSecimiBileseni
import com.example.bitirmev2.components.BackTopBar
import com.example.bitirmev2.data.LocalMessageRepository
import com.example.bitirmev2.data.LocalUserRepository
import com.example.bitirmev2.model.HelpMessage
import com.example.bitirmev2.model.MessageType
import com.example.bitirmev2.nearby.NearbyManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.withContext

@Composable
fun MalzemeYardimFormEkrani(navController: NavHostController) {
    val profile = remember { runBlocking { LocalUserRepository.get() } }

    var city by remember { mutableStateOf(profile?.city ?: "") }
    var district by remember { mutableStateOf(profile?.district ?: "") }
    var neighborhood by remember { mutableStateOf(profile?.neighborhood ?: "") }
    var street by remember { mutableStateOf(profile?.street ?: "") }
    var apartmentNo by remember { mutableStateOf(profile?.apartmentNo ?: "") }

    var extraNote by remember { mutableStateOf("") }

    // Yardım türleri ve miktarları
    var food by remember { mutableStateOf("") }
    var water by remember { mutableStateOf("") }
    var shelter by remember { mutableStateOf("") }
    var heating by remember { mutableStateOf("") }

    var gonderilenMesaj by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()
    val senderFullName = "${profile?.name.orEmpty()} ${profile?.surname.orEmpty()}"

    Column(modifier = Modifier.fillMaxSize()) {
        BackTopBar("Malzeme Yardım Talebi", navController)

        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            //topBar = { BackTopBar("Arama Kurtarma Talebi", navController) }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
            AdresSecimiBileseni(
                initialCity = city,
                initialDistrict = district,
                initialNeighborhood = neighborhood,
                initialStreet = street,
                initialApartment = apartmentNo
            ) { c, d, m, s, a ->
                city = c
                district = d
                neighborhood = m
                street = s
                apartmentNo = a
            }

            Text("İhtiyaç Türleri", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = food,
                onValueChange = { food = it },
                label = { Text("Yiyecek (kişilik)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = water,
                onValueChange = { water = it },
                label = { Text("Su (kişilik)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = shelter,
                onValueChange = { shelter = it },
                label = { Text("Barınma (kişilik)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = heating,
                onValueChange = { heating = it },
                label = { Text("Isınma (kişilik)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = extraNote,
                onValueChange = { extraNote = it },
                label = { Text("Ekstra Açıklama") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val yardimListesi = buildString {
                        if (food.isNotBlank() && food != "0") append("- Yiyecek: ${food} kişilik\n")
                        if (water.isNotBlank() && water != "0") append("- Su: ${water} kişilik\n")
                        if (shelter.isNotBlank() && shelter != "0") append("- Barınma: ${shelter} kişilik\n")
                        if (heating.isNotBlank() && heating != "0") append("- Isınma: ${heating} kişilik\n")
                    }.trim()

                    val message = HelpMessage(
                        id = UUID.randomUUID().toString(),
                        type = MessageType.SUPPLY,
                        name = "", // isim gerekmez
                        address = "$city / $district / $neighborhood / $street / No: $apartmentNo",
                        personCount = 0,
                        extraNote = "Yardım Türleri:\n$yardimListesi\nEkstra Not: $extraNote",
                        senderName = senderFullName
                    )

                    CoroutineScope(Dispatchers.IO).launch {
                        LocalMessageRepository.insert(message)
                        NearbyManager.sendMessageToPeers(message)
                        withContext(Dispatchers.Main) {
                            snackbarHostState.showSnackbar("Mesaj gönderildi")
                        }
                    }

                    gonderilenMesaj = """
                        [GÖNDERİLDİ]
                        Tür: ${message.type}
                        ID: ${message.id}
                        Gönderen: ${message.senderName}
                        Adres: ${message.address}
                        $yardimListesi
                        Ekstra Not: $extraNote
                    """.trimIndent()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Gönder")
            }

            gonderilenMesaj?.let {
                Divider()
                Text(it)
            }
        }
    }
}}
