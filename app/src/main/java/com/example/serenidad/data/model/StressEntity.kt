package com.example.serenidad.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "stress_records",
    indices = [Index(value = ["userId"])]
)
data class StressEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val stressLevel: Int,
    val symptoms: String,
    val note: String?,
    val date: Long,
    val userId: String // <-- UID de Firebase
)