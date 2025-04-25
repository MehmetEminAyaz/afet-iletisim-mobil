package com.example.bitirmev2

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bitirmev2.components.BackTopBar

@Composable
fun YardimSecimEkrani(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize()) {

        // Üst kısımdaki geri tuşu içeren üst bar
        BackTopBar(title = "Yardım Türü Seç", navController = navController)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Yardım Türünü Seçin", fontSize = 22.sp)

            Button(
                onClick = { navController.navigate("aramaKurtarmaForm") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Arama-Kurtarma Talebi")
            }

            Button(
                onClick = { navController.navigate("saglikYardimForm") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sağlık Yardım Talebi")
            }

            Button(
                onClick = { navController.navigate("malzemeYardimForm") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Malzeme Yardım Talebi")
            }

            Button(
                onClick = { navController.navigate("hayattayimForm") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Hayattayım Bildirimi")
            }
        }
    }
}
