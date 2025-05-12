package com.example.animaltrackingui.states

import com.example.animaltrackingui.db.Setting

data class SettingsUiState(
    val settings: List<Setting> = mutableListOf()
)
