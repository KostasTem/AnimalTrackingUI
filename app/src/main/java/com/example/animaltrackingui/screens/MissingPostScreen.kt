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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.animaltrackingui.composables.DeletePostDialog
import com.example.animaltrackingui.composables.PostScreen
import com.example.animaltrackingui.composables.PostScreenBottomBar
import com.example.animaltrackingui.composables.ReportPostDialog
import com.example.animaltrackingui.composables.UpdatePostStatusDialog
import com.example.animaltrackingui.db.MissingPost
import com.example.animaltrackingui.db.PostStatusMap
import com.example.animaltrackingui.ui.theme.applyTonalElevation
import com.example.animaltrackingui.viewModels.AnimalTrackingViewModelProvider
import com.example.animaltrackingui.viewModels.MissingPostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissingPostScreen(
    postID: Long,
    navController: NavHostController,
    missingPostViewModel: MissingPostViewModel = viewModel(factory = AnimalTrackingViewModelProvider.Factory)
) {
    val scrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val uiState = missingPostViewModel.missingPostUiState.collectAsState()
    missingPostViewModel.getPost(postID)
    val missingPost: MissingPost = uiState.value.missingPost
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
                        Text(text = "Missing Post")
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
                    username = missingPost.username,
                    timestamp = missingPost.timestamp!!,
                    animalType = missingPost.animalType,
                    postStatus = missingPost.postStatus,
                    animalImage = missingPost.animalImage,
                    latitude = missingPost.latitude,
                    longitude = missingPost.longitude,
                    location = missingPost.location,
                    hostile = false,
                    animalName = missingPost.animalName,
                    animalAge = missingPost.animalAge,
                    description = missingPost.description,
                    contactInfo = missingPost.contactInfo,
                    comment = null,
                    reportCallback = {
                        selectedAlertDialog = it
                        openAlertDialog = true
                    },
                    paddingValues = it
                )
            }, bottomBar = {
                PostScreenBottomBar(
                    postUsername = missingPost.username,
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
                    missingPostViewModel.deletePost(navController)
                    openAlertDialog = false
                })
            }

            1 -> {
                ReportPostDialog(
                    dismissCallback = { openAlertDialog = false },
                    reportPostCallback = {
                        missingPostViewModel.reportPost()
                        openAlertDialog = false
                    },
                    reasonSelectCallback = {
                        missingPostViewModel.onReportReasonChanged(
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
                        missingPostViewModel.updatePostStatus()
                        openAlertDialog = false
                    },
                    statusSelectCallback = { missingPostViewModel.onPostStatusChanged(PostStatusMap.mapToPostStatus[it]!!) }
                )
            }
        }
    }
}

