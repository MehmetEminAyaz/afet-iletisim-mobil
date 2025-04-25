package com.example.bitirmev2

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bitirmev2.components.BackTopBar
import com.example.bitirmev2.data.LocalMessageRepository
import com.example.bitirmev2.model.HelpMessage
import com.example.bitirmev2.nearby.NearbyManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

@Composable
fun YardimListesiEkrani(navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var helpMessages by remember { mutableStateOf(listOf<HelpMessage>()) }

    fun loadMessages() {
        coroutineScope.launch(Dispatchers.IO) {
            helpMessages = LocalMessageRepository.getAll()
        }
    }

    LaunchedEffect(Unit) { loadMessages() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { BackTopBar("Yardım Çağrıları", navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // 🔴 Tümünü Sil butonu
            Button(
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        LocalMessageRepository.clearAll()
                        loadMessages()
                        withContext(Dispatchers.Main) {
                            snackbarHostState.showSnackbar("Tüm mesajlar silindi")
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.errorContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tümünü Sil")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 📤 Tümünü Gönder butonu
            Button(
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        val allMessages = LocalMessageRepository.getAll()
                        allMessages.forEach { message ->
                            NearbyManager.sendMessageToPeers(message)
                        }
                        withContext(Dispatchers.Main) {
                            snackbarHostState.showSnackbar("${allMessages.size} mesaj gönderildi")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tümünü Gönder (Yakındaki Cihaza)")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(helpMessages) { message ->
                    YardimCard(message) {
                        coroutineScope.launch(Dispatchers.IO) {
                            LocalMessageRepository.deleteById(message.id)
                            loadMessages()
                            withContext(Dispatchers.Main) {
                                snackbarHostState.showSnackbar("Mesaj silindi")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun YardimCard(message: HelpMessage, onDelete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    val relativeTime = remember(message.timestamp) {
        getRelativeTimeString(message.timestamp)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("📌 Tür: ${message.type}", style = MaterialTheme.typography.titleMedium)
                TextButton(onClick = { onDelete() }) {
                    Text("Sil 🗑️", color = MaterialTheme.colorScheme.error)
                }
            }

            Text("👤 Gönderen: ${message.senderName}")
            Text("📍 Adres: ${message.address}")
            Text("⏱️ $relativeTime")

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                Text("👥 Kişi Sayısı: ${message.personCount}")
                Text("🧾 Kişiler: ${message.name}")
                Text("📝 Açıklama: ${message.extraNote}")
                Text("🆔 ID: ${message.id}")
            }
        }
    }
}

fun getRelativeTimeString(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diffMillis = now - timestamp
    val minutes = diffMillis / (1000 * 60)
    val hours = minutes / 60
    val days = hours / 24

    return when {
        minutes < 1 -> "Az önce"
        minutes < 60 -> "$minutes dakika önce"
        hours < 24 -> "$hours saat önce"
        else -> "$days gün önce"
    }
}
