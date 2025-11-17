package com.example.serenidad.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.serenidad.data.dao.MoodDao
import com.example.serenidad.data.dao.SleepDao
import com.example.serenidad.data.model.User
import com.example.serenidad.data.model.MoodEntity
import com.example.serenidad.data.model.SleepEntity
import com.example.serenidad.data.dao.ActivityDao
import com.example.serenidad.data.model.ActivityEntity
import com.example.serenidad.data.dao.StressDao
import com.example.serenidad.data.model.StressEntity
import com.example.serenidad.data.dao.JournalDao
import com.example.serenidad.data.model.JournalEntity

@Database(
    entities = [
        MoodEntity::class,
        SleepEntity::class,
        ActivityEntity::class,
        StressEntity::class,
        JournalEntity::class
    ],
    version = 8,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun moodDao(): MoodDao
    abstract fun sleepDao(): SleepDao
    abstract fun activityDao(): ActivityDao
    abstract fun stressDao(): StressDao
    abstract fun journalDao(): JournalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "serenidad_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}