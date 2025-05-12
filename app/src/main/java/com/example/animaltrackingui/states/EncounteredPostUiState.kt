package com.example.animaltrackingui.states

import com.example.animaltrackingui.db.AppUser
import com.example.animaltrackingui.db.EncounteredPost
import com.example.animaltrackingui.db.PostStatus
import com.example.animaltrackingui.db.Report

data class EncounteredPostUiState(
    val encounteredPost: EncounteredPost = EncounteredPost(),
    val user: AppUser? = null,
    val report: Report = Report(),
    val postStatus: PostStatus = PostStatus.NONE
)