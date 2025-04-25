package com.example.bitirmev2.data

import com.example.bitirmev2.model.HelpMessage

object LocalMessageRepository {
    lateinit var dao: HelpMessageDao

    suspend fun insert(message: HelpMessage) {
        dao.insert(message)
    }

    suspend fun getAll(): List<HelpMessage> {
        return dao.getAll()
    }

    suspend fun deleteById(id: String) {
        dao.deleteById(id)
    }

    suspend fun clearAll() {
        dao.clearAll()
    }

    suspend fun getById(id: String): HelpMessage? {
        return dao.getById(id)
    }
}
