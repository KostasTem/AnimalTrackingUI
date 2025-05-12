package com.example.animaltrackingui.states

import android.net.Uri
import com.example.animaltrackingui.db.Device
import java.time.LocalDate
import java.util.Date

data class RegisterUiState(
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val confirmPassword: String ="",
    val imageUri: Uri = Uri.parse(""),
    val age: LocalDate = LocalDate.now(),
    val countryCode: String = "",
    val device: Device? = null
)