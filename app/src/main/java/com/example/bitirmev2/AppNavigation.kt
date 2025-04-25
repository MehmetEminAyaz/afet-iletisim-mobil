package com.example.bitirmev2

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bitirmev2.screens.*

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainScreen(navController)
        }

        composable("yardimSecimi") {
            YardimSecimEkrani(navController)
        }

        composable("yardimListesi") {
            YardimListesiEkrani(navController)
        }

        composable("aramaKurtarmaForm") {
            AramaKurtarmaFormEkrani(navController)
        }

        composable("kisiselBilgiForm") {
            KisiselBilgiFormuEkrani(navController)
        }

        composable("yakinlarim") {
            YakinlarimEkrani(navController)
        }

        composable("saglikYardimForm") {
            SaglikYardimFormEkrani(navController)
        }

        composable("malzemeYardimForm") {
            MalzemeYardimFormEkrani(navController)
        }

        composable("hayattayimForm") {
            HayattayimFormEkrani(navController)
        }

        composable("login") {
            LoginScreen(navController, LocalContext.current)
        }

        composable("register") {
            RegisterScreen(navController)
        }

        composable("mesajGonder") {
            val context = LocalContext.current
            val token = getTokenFromSharedPreferences(context)
            MesajGondermeEkrani(navController, token)
        }
    }
}

// 🔧 SharedPreferences'tan token alma fonksiyonu
fun getTokenFromSharedPreferences(context: Context): String {
    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    return prefs.getString("token", "") ?: ""
}
