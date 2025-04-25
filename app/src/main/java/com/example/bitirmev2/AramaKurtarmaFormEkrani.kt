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
import com.example.bitirmev2.components.ExposedDropdownField
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
fun AramaKurtarmaFormEkrani(navController: NavHostController) {
    // 🧠 Kullanıcı profilini ilk anda yükle
    val profile = remember {
        runBlocking { LocalUserRepository.get() }
    }

    // Adres bilgileri (ön tanımlı olarak UserProfile'dan alınır)
    var city by remember { mutableStateOf(profile?.city ?: "") }
    var district by remember { mutableStateOf(profile?.district ?: "") }
    var neighborhood by remember { mutableStateOf(profile?.neighborhood ?: "") }
    var street by remember { mutableStateOf(profile?.street ?: "") }
    var apartmentNo by remember { mutableStateOf(profile?.apartmentNo ?: "") }

    var selectedKisiSayisi by remember { mutableStateOf("1") }
    var extraNote by remember { mutableStateOf("") }
    var nameInputs by remember { mutableStateOf(listOf("")) }
    var gonderilenMesaj by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()
    val senderFullName = "${profile?.name.orEmpty()} ${profile?.surname.orEmpty()}"


    Column(modifier = Modifier.fillMaxSize()) {
        BackTopBar("Arama Kurtarma Talebi", navController)

        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
           // topBar = { BackTopBar("Arama Kurtarma Talebi", navController) }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
            // ✅ Adres seçim bileşeni
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

            // Kişi sayısı dropdown
            val kisiSayilari = (1..5).map { it.toString() }
            ExposedDropdownField(
                label = "Kişi Sayısı",
                options = kisiSayilari,
                selectedOption = selectedKisiSayisi,
                onOptionSelected = {
                    selectedKisiSayisi = it
                    nameInputs = List(it.toInt()) { "" }
                }
            )

            // Ad-Soyad girişleri
            nameInputs.forEachIndexed { index, _ ->
                OutlinedTextField(
                    value = nameInputs[index],
                    onValueChange = {
                        nameInputs = nameInputs.toMutableList().apply { set(index, it) }
                    },
                    label = { Text("Kişi ${index + 1} Ad Soyad") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Ekstra açıklama
            OutlinedTextField(
                value = extraNote,
                onValueChange = { extraNote = it },
                label = { Text("Ekstra Açıklama") },
                modifier = Modifier.fillMaxWidth()
            )

            // Gönder butonu
            Button(
                onClick = {
                    val message = HelpMessage(
                        id = UUID.randomUUID().toString(),
                        type = MessageType.RESCUE,
                        name = nameInputs.joinToString(", "),
                        address = "$city / $district / $neighborhood / $street / No: $apartmentNo",
                        personCount = selectedKisiSayisi.toInt(),
                        extraNote = extraNote,
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
                        Kişi Sayısı: ${message.personCount}
                        Kişiler: ${message.name}
                        Açıklama: ${message.extraNote}
                    """.trimIndent()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Gönder")
            }

            // Gönderim sonrası mesaj
            gonderilenMesaj?.let {
                Divider()
                Text(it)
            }
        }
    }
}}
