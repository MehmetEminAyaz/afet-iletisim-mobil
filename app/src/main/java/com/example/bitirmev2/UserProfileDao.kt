package com.example.bitirmev2.data

import androidx.room.*
import com.example.bitirmev2.model.UserProfile

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: UserProfile)

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun get(): UserProfile?

    @Query("DELETE FROM user_profile")
    suspend fun clear()
}
