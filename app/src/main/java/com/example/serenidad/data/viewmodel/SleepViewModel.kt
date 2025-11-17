package com.example.serenidad.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.serenidad.data.database.AppDatabase
import com.example.serenidad.data.model.SleepEntity
import com.example.serenidad.data.repository.SleepRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow // <-- Asegúrate de importar Flow
import kotlinx.coroutines.launch
import java.util.Calendar

class SleepViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SleepRepository

    init {
        // Inicializa el repositorio
        val sleepDao = AppDatabase.getDatabase(application).sleepDao()
        repository = SleepRepository(sleepDao)
    }

    fun addSleepRecord(
        hours: Float,
        quality: String,
        note: String?,
        userId: String // <-- cambiar a String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val newRecord = SleepEntity(
                hours = hours,
                quality = quality,
                note = note,
                date = Calendar.getInstance().timeInMillis,
                userId = userId // ahora UID de Firebase
            )
            repository.insert(newRecord)
        }
    }

    fun getSleepRecords(userId: String): Flow<List<SleepEntity>> {
        return repository.getSleepRecordsForUser(userId)
    }
}