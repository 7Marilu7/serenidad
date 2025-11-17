package com.example.serenidad.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.serenidad.data.database.AppDatabase
import com.example.serenidad.data.model.ActivityEntity
import com.example.serenidad.data.repository.ActivityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Calendar

class ActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ActivityRepository

    init {
        val activityDao = AppDatabase.getDatabase(application).activityDao()
        repository = ActivityRepository(activityDao)
    }
    fun completeActivity(activityName: String, userId: String, note: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            val newActivity = ActivityEntity(
                activityName = activityName,
                dateCompleted = Calendar.getInstance().timeInMillis,
                userId = userId, // ahora String
                note = note
            )
            repository.insert(newActivity)
        }
    }

    fun removeActivity(activityId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            // Usamos el ID de la entidad para asegurar que borramos el correcto
            repository.removeActivityById(activityId)
        }
    }

    fun getCompletedActivities(userId: String): Flow<List<ActivityEntity>> {
        return repository.getCompletedActivities(userId)
    }
    fun isActivityCompleted(userId: String, activityName: String): Flow<Boolean> {
        return repository.isActivityCompleted(userId, activityName)
    }
}