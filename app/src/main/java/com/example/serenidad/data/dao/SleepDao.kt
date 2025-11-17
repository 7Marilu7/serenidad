package com.example.serenidad.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.serenidad.data.model.SleepEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SleepDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSleepRecord(sleep: SleepEntity)

    @Query("SELECT * FROM sleep_records WHERE userId = :userId ORDER BY date DESC")
    fun getSleepRecordsForUser(userId: String): Flow<List<SleepEntity>>
}