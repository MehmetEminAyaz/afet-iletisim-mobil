package com.example.bitirmev2.data

import com.example.bitirmev2.model.NextOfKin

object LocalKinRepository {
    lateinit var dao: NextOfKinDao

    suspend fun add(kin: NextOfKin) = dao.insert(kin)
    suspend fun remove(kin: NextOfKin) = dao.delete(kin)
    suspend fun all(): List<NextOfKin> = dao.getAll()
    suspend fun count(): Int = dao.count()
}
