package com.example.animaltrackingui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.animaltrackingui.composables.DeletePostDialog
import com.example.animaltrackingui.composables.PostScreen
import com.example.animaltrackingui.composables.PostScreenBottomBar
import com.example.animaltrackingui.composables.ReportPostDialog
import com.example.animaltrackingui.composables.UpdatePostStatusDialog
import com.example.animaltrackingui.db.EncounteredPost
import com.example.animaltrackingui.db.PostStatusMap
import com.example.animaltrackingui.ui.theme.AppTheme
import com.example.animaltrackingui.ui.theme.applyTonalElevation
import com.example.animaltrackingui.viewModels.AnimalTrackingViewModelProvider
import com.example.animaltrackingui.viewModels.EncounteredPostViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun EncounteredPostScreen(
    postID: Long,
    navController: NavHostController,
    encounteredPostViewModel: EncounteredPostViewModel = viewModel(factory = AnimalTrackingViewModelProvider.Factory)
) {
    val scrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    encounteredPostViewModel.getPost(postID)
    val uiState = encounteredPostViewModel.encounteredPostUiState.collectAsState()
    val encounteredPost: EncounteredPost = uiState.value.encounteredPost
    var openAlertDialog by remember { mutableStateOf(false) }
    var selectedAlertDialog by remember {
        mutableStateOf(0)
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Encountered Post")
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.navigateUp()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back Button",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    colors = TopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        scrolledContainerColor = MaterialTheme.colorScheme.applyTonalElevation(
                            backgroundColor = MaterialTheme.colorScheme.primary,
                            elevation = 3.dp
                        ),
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    scrollBehavior = scrollBehavior
                )
            },
            content = {
                PostScreen(
                    username = encounteredPost.username,
                    timestamp = encounteredPost.timestamp!!,
                    animalType = encounteredPost.animalType,
                    postStatus = encounteredPost.postStatus,
                    animalImage = encounteredPost.animalImage,
                    latitude = encounteredPost.latitude,
                    longitude = encounteredPost.longitude,
                    location = encounteredPost.location,
                    hostile = encounteredPost.hostile,
                    animalName = null,
                    animalAge = null,
                    description = null,
                    contactInfo = null,
                    comment = encounteredPost.comment,
                    reportCallback = {
                        selectedAlertDialog = it
                        openAlertDialog = true
                    },
                    paddingValues = it
                )
            },
            bottomBar = {
                PostScreenBottomBar(
                    postUsername = encounteredPost.username,
                    user = uiState.value.user,
                    buttonClickCallback = {
                        selectedAlertDialog = it
                        openAlertDialog = true
                    })
            }
        )
    }
    if (openAlertDialog) {
        when (selectedAlertDialog) {
            0 -> {
                DeletePostDialog(dismissCallback = { openAlertDialog = false }, confirmCallback = {
                    encounteredPostViewModel.deletePost(navController)
                    openAlertDialog = false
                })
            }

            1 -> {
                ReportPostDialog(
                    dismissCallback = { openAlertDialog = false },
                    reportPostCallback = {
                        encounteredPostViewModel.reportPost()
                        openAlertDialog = false
                    },
                    reasonSelectCallback = {
                        encounteredPostViewModel.onReportReasonChanged(
                            it
                        )
                    }
                )
            }

            2 -> {
                UpdatePostStatusDialog(
                    selected = uiState.value.postStatus,
                    dismissCallback = { openAlertDialog = false },
                    updatePostStatusCallback = {
                        encounteredPostViewModel.updatePostStatus()
                        openAlertDialog = false
                    },
                    statusSelectCallback = {
                        encounteredPostViewModel.onPostStatusChanged(
                            PostStatusMap.mapToPostStatus[it]!!
                        )
                    }
                )
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    AppTheme {
        EncounteredPostScreen(postID = 1, navController = rememberNavController())
    }
}