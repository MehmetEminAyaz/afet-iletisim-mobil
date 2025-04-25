package com.example.bitirmev2

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token")
    val accessToken: String,

    @SerializedName("tokenType")
    val tokenType: String,

    val id: Long,
    val name: String,
    val email: String,
    val roles: List<String>
)