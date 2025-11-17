package com.example.serenidad.data.repository

import com.example.serenidad.data.dao.MoodDao
import com.example.serenidad.data.model.MoodEntity
import kotlinx.coroutines.flow.Flow

class MoodRepository(private val moodDao: MoodDao) {

    fun getAllMoods(): Flow<List<MoodEntity>> = moodDao.getAllMoods()

    suspend fun insertMood(mood: MoodEntity) {
        moodDao.insertMood(mood)
    }
}