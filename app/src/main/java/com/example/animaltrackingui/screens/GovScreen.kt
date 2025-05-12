package com.example.animaltrackingui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.animaltrackingui.composables.BackendPostFiltering
import com.example.animaltrackingui.composables.EncounteredPostItem
import com.example.animaltrackingui.composables.MissingPostItem
import com.example.animaltrackingui.composables.PostLazyColumn
import com.example.animaltrackingui.composables.TopTabRow
import com.example.animaltrackingui.ui.theme.applyTonalElevation
import com.example.animaltrackingui.viewModels.AnimalTrackingViewModelProvider
import com.example.animaltrackingui.viewModels.GovViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun GovScreen(
    navController: NavHostController,
    govViewModel: GovViewModel = viewModel(factory = AnimalTrackingViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val uiState = govViewModel.govUiState.collectAsState()
    val pagerState = rememberPagerState(initialPage = 0, pageCount = {2})
    val scope = rememberCoroutineScope()
    val encounteredPosts = uiState.value.encounteredPosts
    val missingPosts = uiState.value.missingPosts
    var animalList by remember {
        mutableStateOf(if(uiState.value.periodIndex == 0) missingPosts.map { it.animalType }.distinct() else encounteredPosts.map { it.animalType }.distinct())
    }
    var locationList by remember {
        mutableStateOf(if(uiState.value.periodIndex == 0) missingPosts.map { it.location[2] }.distinct() else encounteredPosts.map { it.location[2] }.distinct())
    }
    LaunchedEffect(key1 = uiState.value.mainUpdated){
        animalList = if(uiState.value.periodIndex == 0) missingPosts.map { it.animalType }.distinct() else encounteredPosts.map { it.animalType }.distinct()
        locationList = if(uiState.value.periodIndex == 0) missingPosts.map { it.location[2] }.distinct() else encounteredPosts.map { it.location[2] }.distinct()
    }
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            govViewModel.updatePeriodIndex(page)
        }
    }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "GOV")
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
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.applyTonalElevation(
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    elevation = 3.dp
                ))
            )
        },
        content = {
            Column(modifier = Modifier.padding(it)) {
                TopTabRow(
                    periodIndex = pagerState.currentPage,
                    onPeriodIndexChanged = {
                            page ->
                        scope.launch {
                            pagerState.animateScrollToPage(page)
                        }
                    }
                )
                HorizontalPager(state = pagerState) {periodIndex ->
                    when (periodIndex) {
                        0 -> PostLazyColumn(
                            items = missingPosts,
                            key = { post -> post.id!! },
                            itemContent = { missingPost ->
                                MissingPostItem(
                                    post = missingPost,
                                    onClick = { navController.navigate("MissingPost/${missingPost.id}") })
                            },
                            onRefresh = {
                                govViewModel.getPosts()
                            },
                            refreshing = uiState.value.refreshing
                        )

                        1 -> PostLazyColumn(
                            items = encounteredPosts,
                            key = { post -> post.id!! },
                            itemContent = { encounteredPost ->
                                EncounteredPostItem(
                                    post = encounteredPost,
                                    onClick = { navController.navigate("EncounteredPost/${encounteredPost.id}") })
                            },
                            onRefresh = {
                                govViewModel.getPosts()
                            },
                            refreshing = uiState.value.refreshing
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            BackendPostFiltering(
                animalList = animalList,
                locationList = locationList,
                sortingCallback = govViewModel::sortingCallback,
                selectedFilterCallback = govViewModel::selectedFilterCallback,
            )
        }
    )
}