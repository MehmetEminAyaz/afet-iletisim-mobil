package com.example.bitirmev2.network

import com.example.bitirmev2.LoginRequest
import com.example.bitirmev2.LoginResponse
import com.example.bitirmev2.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("/api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("/api/auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<Void>
}
