package com.example.bitirmev2

import com.example.bitirmev2.HelpMessageRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface MessageApiService {

    @POST("api/messages")
    suspend fun sendMessage(
        @Body message: HelpMessageRequest, // ✅ Artık doğru formatı gönderiyoruz
        @Header("Authorization") token: String
    ): Response<Void>
}