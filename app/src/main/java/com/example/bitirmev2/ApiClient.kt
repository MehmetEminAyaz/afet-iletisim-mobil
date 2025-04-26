package com.example.bitirmev2

import com.example.bitirmev2.network.AuthService
import com.example.bitirmev2.MessageApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://192.168.1.50:8080/" // ıp

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }

    val messageService: MessageApiService by lazy {
        retrofit.create(MessageApiService::class.java)
    }
}
