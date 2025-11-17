package com.example.serenidad.data.repository

import com.example.serenidad.data.dao.StressDao
import com.example.serenidad.data.model.StressEntity
import kotlinx.coroutines.flow.Flow

class StressRepository(private val stressDao: StressDao) {

    fun getStressRecordsForUser(userId: String): Flow<List<StressEntity>> {
        return stressDao.getStressRecordsForUser(userId)
    }
    suspend fun insert(stress: StressEntity) {
        stressDao.insertStressRecord(stress)
    }
}