package com.example.serenidad.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.serenidad.data.model.ActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: ActivityEntity)
    @Query("SELECT * FROM completed_activities WHERE userId = :userId ORDER BY dateCompleted DESC")
    fun getCompletedActivities(userId: String): Flow<List<ActivityEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM completed_activities WHERE userId = :userId AND activityName = :activityName LIMIT 1)")
    fun isActivityCompleted(userId: String, activityName: String): Flow<Boolean>

    @Query("DELETE FROM completed_activities WHERE id = :activityId")
    suspend fun deleteActivityById(activityId: Int)
}