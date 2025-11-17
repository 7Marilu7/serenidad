package com.example.serenidad.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.serenidad.data.database.AppDatabase
import com.example.serenidad.data.model.StressEntity
import com.example.serenidad.data.repository.StressRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Calendar

class StressViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: StressRepository

    init {
        val stressDao = AppDatabase.getDatabase(application).stressDao()
        repository = StressRepository(stressDao)
    }
    fun addStressRecord(
        stressLevel: Int,
        symptoms: List<String>,
        note: String?,
        userId: String // <-- cambiar a String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val symptomsString = symptoms.joinToString(separator = ",")
            val newRecord = StressEntity(
                stressLevel = stressLevel,
                symptoms = symptomsString,
                note = note,
                date = Calendar.getInstance().timeInMillis,
                userId = userId
            )
            repository.insert(newRecord)
        }
    }
    fun getStressRecords(userId: String): Flow<List<StressEntity>> {
        return repository.getStressRecordsForUser(userId)
    }
}