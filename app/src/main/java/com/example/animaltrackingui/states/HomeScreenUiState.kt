package com.example.animaltrackingui.states

import com.example.animaltrackingui.db.AppUser
import com.example.animaltrackingui.db.Device
import com.example.animaltrackingui.db.EncounteredPost
import com.example.animaltrackingui.db.MissingPost

data class HomeScreenUiState(
    val user: AppUser? = null,
    val device: Device? = null,
    val missingPosts: List<MissingPost> = listOf(),
    val encounteredPosts: List<EncounteredPost> = listOf(),
    val availableAnimals: List<String> = listOf(),
    val availableLocations: List<String> = listOf(),
    val periodIndex: Int = 0,
    val refreshing: Boolean = false,
    val completeAccountInit: Boolean = false,
    val showUserPostsOnly: Boolean = false
)
