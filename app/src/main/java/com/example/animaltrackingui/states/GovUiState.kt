package com.example.animaltrackingui.states

import com.example.animaltrackingui.db.AppUser
import com.example.animaltrackingui.db.Device
import com.example.animaltrackingui.db.EncounteredPost
import com.example.animaltrackingui.db.MissingPost

data class GovUiState(
    val user: AppUser? = null,
    val device: Device? = null,
    val encounteredPosts: List<EncounteredPost> = listOf(),
    val missingPosts: List<MissingPost> = listOf(),
    val periodIndex: Int = 0,
    val mainUpdated: Int = 0,
    val refreshing: Boolean = false
)
