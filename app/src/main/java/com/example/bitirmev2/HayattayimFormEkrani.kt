package com.example.bitirmev2

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bitirmev2.components.BackTopBar
import com.example.bitirmev2.data.LocalKinRepository
import com.example.bitirmev2.data.LocalMessageRepository
import com.example.bitirmev2.data.LocalUserRepository
import com.example.bitirmev2.model.HelpMessage
import com.example.bitirmev2.model.MessageType
import com.example.bitirmev2.nearby.NearbyManager
import kotlinx.coroutines.*
import java.util.*
import kotlinx.coroutines.withContext

@Composable
fun HayattayimFormEkrani(navController: NavHostController) {
    val profile = remember { runBlocking { LocalUserRepository.get() } }
    val scrollState = rememberScrollState()
    val senderFullName = "${profile?.name.orEmpty()} ${profile?.surname.orEmpty()}"
    var gonderilenMesaj by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        BackTopBar("Hayattayım Bildirimi", navController)
        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Aşağıdaki butona basarak hayatta olduğunuzu bildiren bir mesaj gönderirsiniz. Bu mesaj kayıtlı yakınlarınızın e-mail adreslerini içerir ve ana servera ulaştığında onlara iletilecektir.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Button(
                    onClick = {
                        coroutineScope.launch {
                            val profile = LocalUserRepository.get()
                            val contacts = LocalKinRepository.all()

                            if (profile == null) {
                                gonderilenMesaj = "❗ Kayıtlı adres bilgisi bulunamadı!"
                                return@launch
                            }

                            val emailListString = contacts.joinToString("\n- ") { it.email }
                            val emailList = contacts.map { it.email }

                            val message = HelpMessage(
                                id = UUID.randomUUID().toString(),
                                type = MessageType.ALIVE,
                                name = "${profile.name} ${profile.surname}",
                                address = "${profile.city} / ${profile.district} / ${profile.neighborhood} / ${profile.street} / No: ${profile.apartmentNo}",
                                personCount = 1,
                                extraNote = "Yakınlara Bildirilecek E-mailler:\n- $emailListString",
                                senderName = senderFullName,
                                city = profile.city.orEmpty(),
                                district = profile.district.orEmpty(),
                                neighborhood = profile.neighborhood.orEmpty(),
                                street = profile.street.orEmpty(),
                                buildingNumber = profile.apartmentNo.orEmpty(),
                                emergencyContactEmails = emailList // 📌 Artık List<String> gönderdik!
                            )

                            LocalMessageRepository.insert(message)
                            NearbyManager.sendMessageToPeers(message)

                            withContext(Dispatchers.Main) {
                                snackbarHostState.showSnackbar("Mesaj gönderildi")
                            }

                            gonderilenMesaj = """
                                [GÖNDERİLDİ]
                                Tür: ${message.type}
                                ID: ${message.id}
                                Gönderen: ${message.senderName}
                                Adres: ${message.address}
                                Kişi: ${message.name}
                                Yakınlar:
                                - $emailList
                            """.trimIndent()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Hayattayım Bildirimi Gönder")
                }

                gonderilenMesaj?.let {
                    Divider()
                    Text(it)
                }
            }
        }
    }
}
