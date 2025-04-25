package com.example.bitirmev2.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bitirmev2.viewmodel.MessageSenderViewModel
import kotlinx.coroutines.launch

@Composable
fun MesajGondermeEkrani(navController: NavController, token: String) {
    val viewModel = remember { MessageSenderViewModel() }
    val sendState by viewModel.sendState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Bekleyen Yardım Mesajlarını Gönder", style = MaterialTheme.typography.h6)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            viewModel.sendAllMessages(token)
        }) {
            Text("Mesajları Gönder")
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (sendState) {
            is MessageSenderViewModel.SendState.Loading -> {
                CircularProgressIndicator()
            }
            is MessageSenderViewModel.SendState.Success -> {
                val message = (sendState as MessageSenderViewModel.SendState.Success).message
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(message)
                        viewModel.resetState()
                    }
                }
            }
            is MessageSenderViewModel.SendState.Error -> {
                val message = (sendState as MessageSenderViewModel.SendState.Error).message
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(message)
                        viewModel.resetState()
                    }
                }
            }
            else -> {}
        }
    }

    SnackbarHost(hostState = snackbarHostState)
}
