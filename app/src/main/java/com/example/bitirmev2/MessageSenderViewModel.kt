package com.example.bitirmev2.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitirmev2.ApiClient
import com.example.bitirmev2.data.LocalMessageRepository
import com.example.bitirmev2.model.toRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MessageSenderViewModel : ViewModel() {

    private val _sendState = MutableStateFlow<SendState>(SendState.Idle)
    val sendState: StateFlow<SendState> = _sendState

    // ✅ Artık Context alıyoruz, SharedPreferences'tan token çekeceğiz
    fun sendAllMessages(context: Context) {
        viewModelScope.launch {
            _sendState.value = SendState.Loading

            try {
                val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                val token = prefs.getString("token", null)

                if (token.isNullOrBlank()) {
                    _sendState.value = SendState.Error("Token bulunamadı. Lütfen tekrar giriş yapın.")
                    return@launch
                }

                println("KULLANILAN TOKEN: Bearer $token")

                val messages = LocalMessageRepository.getAll()
                var allSuccess = true

                for (message in messages) {
                    println("Gönderiliyor: ${message.toRequest()}")

                    val response = ApiClient.messageService.sendMessage(
                        message = message.toRequest(),
                        token = "Bearer $token"
                    )

                    println("Server cevabı: ${response.code()} ${response.message()}")

                    if (response.isSuccessful) {
                        LocalMessageRepository.deleteById(message.id)
                    } else {
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
