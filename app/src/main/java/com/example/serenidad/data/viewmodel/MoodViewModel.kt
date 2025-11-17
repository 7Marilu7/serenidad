package com.example.serenidad.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.serenidad.data.database.AppDatabase
import com.example.serenidad.data.model.MoodEntity
import com.example.serenidad.data.repository.MoodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MoodViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val repository: MoodRepository = MoodRepository(db.moodDao())

    val allMoods: Flow<List<MoodEntity>> = repository.getAllMoods()

    fun addMood(mood: MoodEntity) {
        viewModelScope.launch {
            repository.insertMood(mood)
        }
    }
}
