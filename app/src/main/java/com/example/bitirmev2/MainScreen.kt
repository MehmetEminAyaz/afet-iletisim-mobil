package com.example.bitirmev2

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun MainScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Afet Sonrası İletişim Sistemi",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate("yardimSecimi") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Yardım Çağrısı Oluştur")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("yardimListesi") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Yardım Çağrılarını Görüntüle")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("kisiselBilgiForm") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Kişisel Bilgilerim")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("yakinlarim") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Yakınlarım")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Divider()

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("login") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Giriş Yap")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("register") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Kayıt Ol")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("mesajGonder") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Bekleyen Mesajları Gönder")
        }
    }
}
