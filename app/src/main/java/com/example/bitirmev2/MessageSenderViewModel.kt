package com.example.bitirmev2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitirmev2.ApiClient
import com.example.bitirmev2.data.LocalMessageRepository
import com.example.bitirmev2.model.HelpMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MessageSenderViewModel : ViewModel() {

    private val _sendState = MutableStateFlow<SendState>(SendState.Idle)
    val sendState: StateFlow<SendState> = _sendState

    fun sendAllMessages(token: String) {
        viewModelScope.launch {
            _sendState.value = SendState.Loading

            try {
                val messages = LocalMessageRepository.getAll()
                var allSuccess = true

                for (message in messages) {
                    println("Gönderiliyor: ${message.id}")
                    val response = ApiClient.messageService.sendMessage(message, "Bearer $token")
                    println("Yanıt kodu: ${response.code()}")
                    if (response.isSuccessful) {
                        LocalMessageRepository.deleteById(message.id)
                    } else {
                        println("Başarısız mesaj: ${response.errorBody()?.string()}")
                        allSuccess = false
                    }
                }

                _sendState.value = if (allSuccess) {
                    SendState.Success("Tüm mesajlar gönderildi.")
                } else {
                    SendState.Error("Bazı mesajlar gönderilemedi.")
                }
            } catch (e: Exception) {
                _sendState.value = SendState.Error("Gönderim sırasında hata oluştu: ${e.message}")
            }
        }
    }

    fun resetState() {
        _sendState.value = SendState.Idle
    }

    sealed class SendState {
        object Idle : SendState()
        object Loading : SendState()
        data class Success(val message: String) : SendState()
        data class Error(val message: String) : SendState()
    }
}
