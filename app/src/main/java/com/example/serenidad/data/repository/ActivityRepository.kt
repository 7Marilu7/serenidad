package com.example.serenidad.data.repository

import com.example.serenidad.data.dao.ActivityDao
import com.example.serenidad.data.model.ActivityEntity
import kotlinx.coroutines.flow.Flow

class ActivityRepository(private val activityDao: ActivityDao) {

    fun getCompletedActivities(userId: String): Flow<List<ActivityEntity>> {
        return activityDao.getCompletedActivities(userId)
    }

    fun isActivityCompleted(userId: String, activityName: String): Flow<Boolean> {
        return activityDao.isActivityCompleted(userId, activityName)
    }

    suspend fun insert(activity: ActivityEntity) {
        activityDao.insertActivity(activity)
    }

    suspend fun removeActivityById(activityId: Int) {
        activityDao.deleteActivityById(activityId)
    }
}