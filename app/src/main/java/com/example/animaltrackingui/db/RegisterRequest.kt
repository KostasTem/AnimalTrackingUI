package com.example.animaltrackingui.db

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val imagePath: String?,
    val age: String?,
    val countryCode: String?
)
