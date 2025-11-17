package com.example.serenidad.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.serenidad.data.database.AppDatabase
import com.example.serenidad.data.model.JournalEntity
import com.example.serenidad.data.repository.JournalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Calendar

class JournalViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: JournalRepository

    init {
        val journalDao = AppDatabase.getDatabase(application).journalDao()
        repository = JournalRepository(journalDao)
    }

    fun getJournalEntries(userId: String): Flow<List<JournalEntity>> {
        return repository.getEntriesForUser(userId)
    }
    fun addEntry(content: String, colorHex: String, userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val newEntry = JournalEntity(
                content = content,
                colorHex = colorHex,
                date = Calendar.getInstance().timeInMillis,
                userId = userId // ahora String
            )
            repository.insert(newEntry)
        }
    }

    fun updateEntry(entry: JournalEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            // Re-asigna la fecha de "actualización"
            repository.update(entry.copy(date = Calendar.getInstance().timeInMillis))
        }
    }

    fun deleteEntry(entryId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteById(entryId)
        }
    }
}