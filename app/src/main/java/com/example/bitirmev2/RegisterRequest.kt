package com.example.bitirmev2

data class RegisterRequest(
    val name: String,
    val surname: String,
    val email: String,
    val password: String,
    val role: String = "USER"
)
