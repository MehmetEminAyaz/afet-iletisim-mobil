package com.example.bitirmev2 // Eğer klasör ismi farklıysa bunu uyarlamalısın

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bitirmev2.LoginViewModel
import androidx.compose.ui.graphics.Color

@Composable
fun LoginScreen(navController: NavController, context: Context) {
    val viewModel: LoginViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LoginViewModel(context) as T
        }
    })

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    if (viewModel.loginResult.value) {
        LaunchedEffect(Unit) {
            navController.navigate("main") {
                popUpTo("login") { inclusive = true } // geri tuşuyla login'e dönülmesin
            }
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text("Giriş Yap", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = email.value, onValueChange = { email.value = it }, label = { Text("Email") })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = password.value, onValueChange = { password.value = it }, label = { Text("Şifre") }, visualTransformation = PasswordVisualTransformation())

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.login(email.value, password.value) }, modifier = Modifier.fillMaxWidth()) {
            Text("Giriş Yap")
        }

        if (viewModel.errorMessage.value.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(viewModel.errorMessage.value, color = Color.Red)
        }
    }
}
