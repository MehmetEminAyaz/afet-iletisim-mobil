package com.example.bitirmev2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bitirmev2.model.HelpMessage
import com.example.bitirmev2.model.NextOfKin
import com.example.bitirmev2.model.UserProfile
import com.example.bitirmev2.model.Converters //  yeni yazacağımız Converters sınıfı

@Database(
    entities = [HelpMessage::class, UserProfile::class, NextOfKin::class],
    version = 2, //
    exportSchema = false
)
@TypeConverters(Converters::class) //
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
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
