package com.example.serenidad.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "moods")
data class MoodEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val emotion: String,
    val note: String? = null,
    val date: Long = System.currentTimeMillis()
)