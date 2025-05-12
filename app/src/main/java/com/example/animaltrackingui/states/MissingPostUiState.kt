package com.example.animaltrackingui.states

import com.example.animaltrackingui.db.AppUser
import com.example.animaltrackingui.db.MissingPost
import com.example.animaltrackingui.db.PostStatus
import com.example.animaltrackingui.db.Report

data class MissingPostUiState(
    val missingPost: MissingPost = MissingPost(),
    val user: AppUser? = null,
    val report: Report = Report(),
    val postStatus: PostStatus = PostStatus.NONE
)