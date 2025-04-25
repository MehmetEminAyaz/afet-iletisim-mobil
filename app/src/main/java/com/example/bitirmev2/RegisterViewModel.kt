package com.example.bitirmev2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.bitirmev2.UserAuthRepository

class RegisterViewModel : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun register(name: String, surname: String, email: String, password: String) {
        _registerState.value = RegisterState.Loading

        viewModelScope.launch {
            val success = UserAuthRepository.register(name, surname, email, password)
            if (success) {
                _registerState.value = RegisterState.Success
            } else {
                _registerState.value = RegisterState.Error("Kayıt başarısız oldu. Lütfen tekrar deneyin.")
            }
        }
    }

    fun resetState() {
        _registerState.value = RegisterState.Idle
    }

    sealed class RegisterState {
        object Idle : RegisterState()
        object Loading : RegisterState()
        object Success : RegisterState()
        data class Error(val message: String) : RegisterState()
    }
}
