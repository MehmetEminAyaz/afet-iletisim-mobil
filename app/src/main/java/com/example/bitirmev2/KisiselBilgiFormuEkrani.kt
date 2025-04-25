package com.example.bitirmev2.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bitirmev2.components.AdresSecimiBileseni
import com.example.bitirmev2.components.BackTopBar
import com.example.bitirmev2.data.LocalUserRepository
import com.example.bitirmev2.model.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun KisiselBilgiFormuEkrani(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }

    var city by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var neighborhood by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var apartmentNo by remember { mutableStateOf("") }

    var savedProfile by remember { mutableStateOf<UserProfile?>(null) }

    // Daha önce kayıtlı profil varsa formu otomatik doldur
    LaunchedEffect(Unit) {
        val existing = LocalUserRepository.get()
        existing?.let {
            name = it.name
            surname = it.surname
            city = it.city
            district = it.district
            neighborhood = it.neighborhood
            street = it.street
            apartmentNo = it.apartmentNo
            savedProfile = it
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        BackTopBar("Kişisel Bilgi Formu", navController)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Ad") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = surname,
                onValueChange = { surname = it },
                label = { Text("Soyad") },
                modifier = Modifier.fillMaxWidth()
            )

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

            Button(
                onClick = {
                    val profile = UserProfile(
                        id = 1,
                        name = name,
                        surname = surname,
                        city = city,
                        district = district,
                        neighborhood = neighborhood,
                        street = street,
                        apartmentNo = apartmentNo
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        LocalUserRepository.save(profile)
                        savedProfile = profile
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kaydet")
            }

            // Kaydedilen bilgileri göster
            savedProfile?.let {
                Divider(modifier = Modifier.padding(top = 16.dp))
                Text("✅ Kaydedilen Bilgiler:", style = MaterialTheme.typography.titleSmall)
                Text("Ad: ${it.name}")
                Text("Soyad: ${it.surname}")
                Text("Adres: ${it.city} / ${it.district} / ${it.neighborhood} / ${it.street} / No: ${it.apartmentNo}")
            }
        }
    }
}
