package com.example.animaltrackingui.states

import com.example.animaltrackingui.db.AppUser
import java.time.LocalDate

data class CompleteFirstGoogleSignInUiState(
    val user: AppUser? = null,
    val age: LocalDate = LocalDate.now(),
    val countryCode: String = ""
)
