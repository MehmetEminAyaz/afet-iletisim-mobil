package com.example.bitirmev2

import android.content.Context
import android.util.Log
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitirmev2.UserAuthRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class LoginViewModel(private val context: Context) : ViewModel() {

    private val _loginResult = mutableStateOf(false)
    val loginResult: State<Boolean> = _loginResult

    private val _errorMessage = mutableStateOf("")
    val errorMessage: State<String> = _errorMessage

    fun login(email: String, password: String) {
        viewModelScope.launch {
            Log.d("LOGIN_DEBUG", "Gönderilen email: $email, password: $password")  // 👈 BURASI

            val token = UserAuthRepository.login(email, password)
            if (token != null) {
                saveTokenToPrefs(token)
                _loginResult.value = true
            } else {
                _errorMessage.value = "Giriş başarısız."
            }
        }
    }

    private fun saveTokenToPrefs(token: String) {
        val prefs: SharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        prefs.edit().putString("token", token).apply()
    }
}
