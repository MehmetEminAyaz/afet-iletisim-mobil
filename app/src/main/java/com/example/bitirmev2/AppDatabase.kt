package com.example.bitirmev2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bitirmev2.model.HelpMessage
import com.example.bitirmev2.model.NextOfKin
import com.example.bitirmev2.model.UserProfile

@Database(
    entities = [HelpMessage::class, UserProfile::class, NextOfKin::class],
    version = 2, // ✅ Şema güncellendiği için versiyon artırıldı
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun helpMessageDao(): HelpMessageDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun nextOfKinDao(): NextOfKinDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "help_message_db"
                )
                    .fallbackToDestructiveMigration() // ✅ Versiyon uyumsuzluğu varsa, eski veriyi siler
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
