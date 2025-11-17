package com.example.serenidad.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "journal_entries",
    indices = [Index(value = ["userId"])]
)
data class JournalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val content: String,
    val colorHex: String,
    val date: Long,
    val userId: String // <-- UID de Firebase
)