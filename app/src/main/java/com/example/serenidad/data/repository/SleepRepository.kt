package com.example.serenidad.data.repository

import com.example.serenidad.data.dao.SleepDao
import com.example.serenidad.data.model.SleepEntity
import kotlinx.coroutines.flow.Flow

class SleepRepository(private val sleepDao: SleepDao) {

    // Obtiene el flujo de registros de sueño para un usuario
    fun getSleepRecordsForUser(userId: String): Flow<List<SleepEntity>> {
        return sleepDao.getSleepRecordsForUser(userId)
    }
    // Inserta un nuevo registro de sueño
    suspend fun insert(sleep: SleepEntity) {
        sleepDao.insertSleepRecord(sleep)
    }
}