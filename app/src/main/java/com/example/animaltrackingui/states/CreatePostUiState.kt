package com.example.animaltrackingui.states

import android.net.Uri
import com.example.animaltrackingui.db.AppUser

data class CreatePostUiState(
    val animalType: String = "Loading Animals...",
    val animalName: String = "",
    val animalAge: String = "",
    val contactInfo: String = "",
    val text: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val animalImageUri: Uri = Uri.parse(""),
    val location: List<String> = listOf("", "", "", ""),
    val user: AppUser = AppUser(),
    val openAlertDialog: Boolean = false,
    val showBottomSheet: Boolean = false,
    val showSelectedImage: Boolean = false,
    val showSelectedLocation: Boolean = false,
    val showIdentificationDialog: Boolean = false,
    val identifiedAnimal: String? = null,
    val selectedBottomSheet: Int = 1,
    val hostile: Boolean = false,
    val animalTypeList: List<String> = listOf("Loading Animals..."),
    val postType: PostType = PostType.NONE
)

enum class PostType{
    NONE, MISSING, ENCOUNTERED
}