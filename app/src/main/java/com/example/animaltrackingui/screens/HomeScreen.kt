package com.example.animaltrackingui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.animaltrackingui.R
import com.example.animaltrackingui.composables.AccountContextMenu
import com.example.animaltrackingui.composables.BackendPostFiltering
import com.example.animaltrackingui.composables.EncounteredPostItem
import com.example.animaltrackingui.composables.HomeScreenActionBottomSheet
import com.example.animaltrackingui.composables.MissingPostItem
import com.example.animaltrackingui.composables.PostLazyColumn
import com.example.animaltrackingui.composables.PostMap
import com.example.animaltrackingui.composables.TopTabRow
import com.example.animaltrackingui.db.BottomNavigation
import com.example.animaltrackingui.db.getCountryLatLng
import com.example.animaltrackingui.getActivity
import com.example.animaltrackingui.ui.theme.applyTonalElevation
import com.example.animaltrackingui.viewModels.AnimalTrackingViewModelProvider
import com.example.animaltrackingui.viewModels.HomeScreenViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    homeScreenViewModel: HomeScreenViewModel = viewModel(factory = AnimalTrackingViewModelProvider.Factory)
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val homeScreenNavController = rememberNavController()
    val uiState = homeScreenViewModel.homeScreenUiState.collectAsState()
    val missingPosts = uiState.value.missingPosts
    val encounteredPosts = uiState.value.encounteredPosts
    val pagerState = rememberPagerState(pageCount = { 2 }, initialPage = 0)
    val sheetState = rememberModalBottomSheetState(
        confirmValueChange = { it != SheetValue.PartiallyExpanded },
        skipPartiallyExpanded = true,
    )
    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = uiState.value.completeAccountInit) {
        if (uiState.value.completeAccountInit) {
            navController.navigate("CompleteFirstGoogleSignIn")
        }
    }
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            homeScreenViewModel.updatePeriodIndex(page)
        }
    }
    BackHandler {
        if (showBottomSheet) {
            showBottomSheet = false
        }
        else{
            context.getActivity()?.finish()
        }
    }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Icon(
                        painter = painterResource(id = R.drawable.pet),
                        contentDescription = "Pet",
                        modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally)
                    )
                },
                navigationIcon = {
                    if (uiState.value.user == null) {
                        IconButton(
                            onClick = {
                                navController.navigate("Login")
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.account_circle),
                                contentDescription = "Account",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else {
                        AccountContextMenu(
                            navController = navController,
                            onLogoutClick = homeScreenViewModel::logout,
                            user = uiState.value.user!!,
                            imagePath = uiState.value.user?.imagePath,
                            showUserPostsOnly = uiState.value.showUserPostsOnly,
                            onShowUserPostsOnlyChanged = homeScreenViewModel::onShowUserPostsOnlyChanged
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("Settings") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.settings),
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.applyTonalElevation(
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    elevation = 3.dp
                ))
            )
        },
        content = {
            NavHost(
                navController = homeScreenNavController,
                startDestination = "Posts",
                modifier = Modifier.padding(it)
            ) {
                composable("Posts") {
                    OnLifecycleEvent{ _, event ->
                        when(event){
                            Lifecycle.Event.ON_RESUME -> {
                                homeScreenViewModel.updateDevice()
                            }
                            else -> {
                                return@OnLifecycleEvent
                            }
                        }
                    }
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        TopTabRow(
                            periodIndex = pagerState.currentPage,
                            onPeriodIndexChanged = { page ->
                                scope.launch {
                                    pagerState.animateScrollToPage(page)
                                }
                            }
                        )
                        HorizontalPager(state = pagerState) { periodIndex ->
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
                                        homeScreenViewModel.getPosts(defaultParams = true)
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
                                        homeScreenViewModel.getPosts(defaultParams = true)
                                    },
                                    refreshing = uiState.value.refreshing
                                )
                            }
                        }
                        if (showBottomSheet) {
                            HomeScreenActionBottomSheet(
                                navController = navController,
                                sheetState = sheetState,
                                user = uiState.value.user,
                                showBottomSheetCallback = { showSheet ->
                                    showBottomSheet = showSheet
                                }
                            )
                        }
                    }
                }
                composable("PostMap") {
                    val userLoc by remember {
                        mutableStateOf(getCountryLatLng(uiState.value.device?.countryCode!!)!!)
                    }
                    PostMap(
                        mapCenter = userLoc,
                        countryCode = uiState.value.device?.countryCode!!,
                        navController = navController,
                        fetchPosts = homeScreenViewModel::fetchPosts
                    )
                    if (showBottomSheet) {
                        HomeScreenActionBottomSheet(
                            navController = navController,
                            sheetState = sheetState,
                            user = uiState.value.user,
                            showBottomSheetCallback = { showSheet ->
                                showBottomSheet = showSheet
                            }
                        )
                    }
                }
            }

        },
        bottomBar = {
            BottomBar(
                homeScreenNavController = homeScreenNavController,
                callback = { showBottomSheet = true })
        },
        floatingActionButton = {
            val navBackStackEntry by homeScreenNavController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            if (currentRoute == "Posts") {
                Column {
                    BackendPostFiltering(
                        animalList = uiState.value.availableAnimals,
                        locationList = uiState.value.availableLocations,
                        sortingCallback = homeScreenViewModel::sortingCallback,
                        selectedFilterCallback = homeScreenViewModel::selectedFilterCallback,
                    )
                }
            }
        }
    )

}

@Composable
private fun BottomBar(homeScreenNavController: NavHostController, callback: () -> Unit) {
    val navBackStackEntry by homeScreenNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val bottomNavigationItems =
        listOf(BottomNavigation.Posts, BottomNavigation.PostMap, BottomNavigation.AddObject)
    NavigationBar {
        bottomNavigationItems.forEach {
            NavigationBarItem(
                selected = currentRoute == it.route,
                onClick = {
                    if (currentRoute != it.route && it.route != "AddObject") {
                        homeScreenNavController.navigate(it.route)
                    }
                    if (it.route == "AddObject") {
                        callback()
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = it.resource),
                        contentDescription = ""
                    )
                },
                label = { Text(text = it.label) })
        }
    }
}

@Composable
private fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}


