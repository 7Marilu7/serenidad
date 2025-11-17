package com.example.serenidad.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.serenidad.data.model.StressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStressRecord(stress: StressEntity)

    @Query("SELECT * FROM stress_records WHERE userId = :userId ORDER BY date DESC")
    fun getStressRecordsForUser(userId: String): Flow<List<StressEntity>>
}