package com.example.serenidad.data.model

data class User(
    val uid: String = "",
    val email: String = "",
    val password: String = "",
    val isAdmin: Boolean = false
)
