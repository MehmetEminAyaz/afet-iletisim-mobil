package com.example.bitirmev2.data

import androidx.room.*
import com.example.bitirmev2.model.NextOfKin

@Dao
interface NextOfKinDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(kin: NextOfKin)

    @Delete
    suspend fun delete(kin: NextOfKin)

    @Query("SELECT * FROM next_of_kin ORDER BY `order` ASC")
    suspend fun getAll(): List<NextOfKin>

    @Query("SELECT COUNT(*) FROM next_of_kin")
    suspend fun count(): Int
}
