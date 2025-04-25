package com.example.bitirmev2.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bitirmev2.components.BackTopBar
import com.example.bitirmev2.data.LocalKinRepository
import com.example.bitirmev2.model.NextOfKin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun YakinlarimEkrani(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var kinList by remember { mutableStateOf(listOf<NextOfKin>()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Mevcut kişileri yükle
    LaunchedEffect(Unit) {
        kinList = LocalKinRepository.all()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        BackTopBar("Yakınlarım", navController)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Yakının Adı Soyadı") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Yakının E-posta Adresi") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        val currentCount = LocalKinRepository.count()
                        if (currentCount >= 3) {
                            errorMessage = "En fazla 3 yakın eklenebilir."
                        } else if (name.isBlank() || email.isBlank()) {
                            errorMessage = "Lütfen tüm alanları doldurun."
                        } else {
                            LocalKinRepository.add(
                                NextOfKin(
                                    name = name,
                                    email = email,
                                    order = currentCount + 1
                                )
                            )
                            name = ""
                            email = ""
                            kinList = LocalKinRepository.all()
                            errorMessage = null
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Yakın Ekle")
            }

            errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Divider()

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(kinList) { kin ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("👤 ${kin.name}")
                            Text("✉️ ${kin.email}")
                            Button(
                                onClick = {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        LocalKinRepository.remove(kin)
                                        kinList = LocalKinRepository.all()
                                    }
                                },
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Text("Sil")
                            }
                        }
                    }
                }
            }
        }
    }
}
