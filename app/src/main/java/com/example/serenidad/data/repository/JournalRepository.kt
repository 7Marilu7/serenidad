package com.example.serenidad.data.repository

import com.example.serenidad.data.dao.JournalDao
import com.example.serenidad.data.model.JournalEntity
import kotlinx.coroutines.flow.Flow

class JournalRepository(private val journalDao: JournalDao) {

    fun getEntriesForUser(userId: String): Flow<List<JournalEntity>> {
        return journalDao.getEntriesForUser(userId)
    }

    suspend fun insert(entry: JournalEntity) {
        journalDao.insertEntry(entry)
    }

    suspend fun update(entry: JournalEntity) {
        journalDao.updateEntry(entry)
    }

    suspend fun deleteById(entryId: Int) {
        journalDao.deleteEntryById(entryId)
    }
}