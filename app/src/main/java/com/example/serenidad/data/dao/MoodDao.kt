package com.example.serenidad.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.serenidad.data.model.MoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {

    @Insert
    suspend fun insertMood(mood: MoodEntity)

    @Query("SELECT * FROM moods ORDER BY date DESC")
    fun getAllMoods(): Flow<List<MoodEntity>>
}