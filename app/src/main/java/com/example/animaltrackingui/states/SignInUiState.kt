package com.example.animaltrackingui.states

import com.example.animaltrackingui.db.Device


data class SignInUiState(
    val email: String = "",
    val password: String = "",
    val device: Device? = null,
    val staySignedIn: Boolean = false
)
