package com.example.bitirmev2

import android.util.Log
import com.example.bitirmev2.LoginRequest
import com.example.bitirmev2.RegisterRequest
import com.example.bitirmev2.LoginResponse
import com.example.bitirmev2.ApiClient

object UserAuthRepository {

    suspend fun login(email: String, password: String): String? {
        return try {
            val response = ApiClient.authService.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                Log.d("LOGIN_SUCCESS", "Token: ${response.body()?.accessToken}")
                response.body()?.accessToken
            } else {
                Log.e("LOGIN_FAIL", "Code: ${response.code()}, Error: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("LOGIN_EXCEPTION", "Exception: ${e.localizedMessage}")
            null
        }
    }

    suspend fun register(name: String, surname: String, email: String, password: String): Boolean {
        return try {
            val request = RegisterRequest(name, surname, email, password)
            val response = ApiClient.authService.register(request)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}
