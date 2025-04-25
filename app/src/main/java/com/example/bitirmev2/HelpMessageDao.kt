package com.example.bitirmev2.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bitirmev2.model.HelpMessage

@Dao
interface HelpMessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: HelpMessage)

    @Query("SELECT * FROM HelpMessage ORDER BY timestamp DESC")
    suspend fun getAll(): List<HelpMessage>

    @Query("DELETE FROM HelpMessage WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM HelpMessage")
    suspend fun clearAll()

    @Query("SELECT * FROM HelpMessage WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): HelpMessage?
}
