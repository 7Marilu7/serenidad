package com.example.serenidad.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "sleep_records",
    indices = [Index(value = ["userId"])]
)
data class SleepEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val hours: Float,
    val quality: String,
    val note: String?,
    val date: Long,
    val userId: String // <-- UID de Firebase
)