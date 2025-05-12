package com.example.animaltrackingui

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.animaltrackingui.composables.ConnectivityStatus
import com.example.animaltrackingui.retrofit.RetrofitObject
import com.example.animaltrackingui.screens.CompleteGoogleSignInScreen
import com.example.animaltrackingui.screens.EncounteredPostCreateScreen
import com.example.animaltrackingui.screens.EncounteredPostScreen
import com.example.animaltrackingui.screens.GovScreen
import com.example.animaltrackingui.screens.HomeScreen
import com.example.animaltrackingui.screens.InitScreen
import com.example.animaltrackingui.screens.LoginScreen
import com.example.animaltrackingui.screens.MissingPostCreateScreen
import com.example.animaltrackingui.screens.MissingPostScreen
import com.example.animaltrackingui.screens.RegisterScreen
import com.example.animaltrackingui.screens.SettingsScreen
import com.example.animaltrackingui.ui.theme.AppTheme


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val prefs = applicationContext.getSharedPreferences("ATData", Context.MODE_PRIVATE)
        val initialized = prefs.contains("initialized")
        setContent {
            AppTheme {
                val startDestination by remember {
                    mutableStateOf(if(initialized)"Main" else "Init")
                }
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        val blockRequest = RetrofitObject.blockRequest.collectAsState()
                        ConnectivityStatus(isConnected = blockRequest.value.not())
                        val navController = rememberNavController()
                        NavHost(
                            navController = navController,
                            startDestination = startDestination
                        ) {
                            composable(route = "Init", deepLinks = listOf(navDeepLink { uriPattern = "animal_tracking_app://init" })){
                                InitScreen(navController = navController)
                            }
                            composable(route = "Main", deepLinks = listOf(navDeepLink { uriPattern = "animal_tracking_app://main" })) {
                                HomeScreen(navController = navController)
                            }
                            composable(route = "Login") {
                                LoginScreen(navController = navController)
                            }
                            composable(route = "Register") {
                                RegisterScreen(navController = navController)
                            }
                            composable(
                                route = "MissingPost/{postID}",
                                arguments = listOf(navArgument("postID") {
                                    type = NavType.LongType
                                }),
                                deepLinks = listOf(navDeepLink { uriPattern = "animal_tracking_app://missing_post/{postID}" })
                            ) {
                                val postID = it.arguments?.getLong("postID")
                                if (postID != null) {
                                    MissingPostScreen(
                                        postID = postID,
                                        navController = navController
                                    )
                                }
                            }
                            composable(
                                route = "EncounteredPost/{postID}",
                                arguments = listOf(navArgument("postID") {
                                    type = NavType.LongType
                                }),
                                deepLinks = listOf(navDeepLink { uriPattern = "animal_tracking_app://encountered_post/{postID}" })
                            ) {
                                val postID = it.arguments?.getLong("postID")
                                if (postID != null) {
                                    EncounteredPostScreen(
                                        postID = postID,
                                        navController = navController
                                    )
                                }
                            }
                            composable(route = "Settings") {
                                SettingsScreen(navController = navController)
                            }
                            composable(route = "EncounteredPostCreate") {
                                EncounteredPostCreateScreen(navController = navController)
                            }
                            composable(route = "MissingPostCreate") {
                                MissingPostCreateScreen(navController = navController)
                            }
                            composable(route = "GovUI"){
                                GovScreen(navController = navController)
                            }
                            composable(route = "CompleteFirstGoogleSignIn"){
                                CompleteGoogleSignInScreen(navController = navController)
                            }
                        }
                    }
                }

            }
        }
    }
}

fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}
