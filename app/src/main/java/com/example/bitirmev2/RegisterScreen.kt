package com.example.bitirmev2.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bitirmev2.RegisterViewModel

@Composable
fun RegisterScreen(navController: NavController) {
    val viewModel = remember { RegisterViewModel() }
    val registerState by viewModel.registerState.collectAsState()

    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Kayıt Ol", style = MaterialTheme.typography.h5)

        Spacer(modifier = Modifier.height(16.dp))

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
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-posta") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Şifre") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            viewModel.register(name, surname, email, password)
        }) {
            Text("Kayıt Ol")
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = {
            navController.navigate("login")
        }) {
            Text("Zaten hesabın var mı? Giriş yap")
        }

        when (registerState) {
            is RegisterViewModel.RegisterState.Loading -> {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }
            is RegisterViewModel.RegisterState.Success -> {
                LaunchedEffect(Unit) {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            }
            is RegisterViewModel.RegisterState.Error -> {
                val errorMessage = (registerState as RegisterViewModel.RegisterState.Error).message
                Text(text = errorMessage, color = MaterialTheme.colors.error)
            }
            else -> {}
        }
    }
}
