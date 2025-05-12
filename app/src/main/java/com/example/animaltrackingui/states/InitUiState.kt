package com.example.animaltrackingui.states

import com.example.animaltrackingui.db.Device
import com.example.animaltrackingui.db.Setting

data class InitUiState(
    val device: Device? = null,
    val missingNotificationSetting: Setting? = null,
    val encounteredNotificationSetting: Setting? = null,
    val localizationSetting: Setting? = null
)
