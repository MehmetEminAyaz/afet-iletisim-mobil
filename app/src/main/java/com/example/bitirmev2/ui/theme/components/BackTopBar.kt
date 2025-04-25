@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.bitirmev2.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@Composable
fun BackTopBar(title: String, navController: NavHostController) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Geri Dön")
            }
        }
        // tonalElevation = 4.dp ← bunu kaldır
    )
}
