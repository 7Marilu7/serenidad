package com.example.serenidad.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "completed_activities",
    indices = [Index(value = ["userId"])]
)
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val activityName: String,
    val dateCompleted: Long,
    val userId: String, // <-- ahora es UID de Firebase
    val note: String? = null
)
