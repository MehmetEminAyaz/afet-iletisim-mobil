package com.example.bitirmev2.data

import com.example.bitirmev2.model.UserProfile

object LocalUserRepository {
    lateinit var dao: UserProfileDao

    suspend fun save(profile: UserProfile) = dao.insert(profile)

    suspend fun get(): UserProfile? = dao.get()
}
