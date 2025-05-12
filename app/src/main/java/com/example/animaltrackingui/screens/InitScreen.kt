package com.example.animaltrackingui.screens

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.animaltrackingui.R
import com.example.animaltrackingui.composables.CountryCodeDialog
import com.example.animaltrackingui.db.Setting
import com.example.animaltrackingui.db.getListOfCountries
import com.example.animaltrackingui.viewModels.AnimalTrackingViewModelProvider
import com.example.animaltrackingui.viewModels.InitViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InitScreen(
    navController: NavHostController,
    initViewModel: InitViewModel = viewModel(factory = AnimalTrackingViewModelProvider.Factory)
) {
    val uiState = initViewModel.initUiState.collectAsState()
    val scope = rememberCoroutineScope()
    val titles by remember {
        mutableStateOf(
            listOf(
                "Welcome To The Animal Tracking App",
                "Localization",
                "Notifications",
                "Begin Posting"
            )
        )
    }
    val descriptions by remember {
        mutableStateOf(
            listOf(
                "Find Your Lost Animals And Help Others Find Theirs.",
                "Select The Country That You Should Receive Posts For",
                "Select Which Notifications You Would Like To Receive",
                "Login Using Your Existing Account Or Register For A New One. You Can Also Jump Straight To The App To Explore The Posts Uploaded By Our Users"
            )
        )
    }
    val pagerState = rememberPagerState(
        pageCount = { titles.size }
    )
    var selectedCountry by remember {
        mutableStateOf("")
    }
    val missingSetting = uiState.value.missingNotificationSetting
    val encounteredSetting = uiState.value.encounteredNotificationSetting;
    val localizationSetting = uiState.value.localizationSetting
    val context = LocalContext.current
    BackHandler {
        if (pagerState.currentPage == 0) {
            return@BackHandler
        } else {
            scope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage - 1)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.wrapContentSize()
        ) { currentPage ->
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(36.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = titles[currentPage],
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = descriptions[currentPage],
                    modifier = Modifier.padding(top = 45.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
        when (pagerState.currentPage) {
            1 -> {
                SelectCountry(callback = {
                    selectedCountry = it
                    if (localizationSetting != null) {
                        initViewModel.updateSetting(localizationSetting, it)
                    }
                })
            }

            2 -> {
                SelectNotifications(
                    missingSetting = missingSetting,
                    encounteredSetting = encounteredSetting,
                    updateSettingCallback = initViewModel::updateSetting
                )
            }

            3 -> {
                LoginRegisterBox(
                    navController = navController,
                    onInitDevice = { initViewModel.completeInitialization(context) })
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        PageIndicator(
            pageCount = titles.size,
            currentPage = pagerState.currentPage,
            modifier = Modifier.padding(60.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
    }
    ButtonsSection(
        pagerState = pagerState,
        navController = navController,
        onInitDevice = { initViewModel.completeInitialization(context) }
    )

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ButtonsSection(
    pagerState: PagerState,
    navController: NavHostController,
    onInitDevice: () -> Unit
) {

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(20.dp)
    ) {
        if (pagerState.currentPage != 3) {
            Text(
                text = "Next",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .clickable {

                        scope.launch {
                            val nextPage = pagerState.currentPage + 1
                            pagerState.scrollToPage(nextPage)
                        }
                    },
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Back",
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .clickable {
                        scope.launch {
                            val prevPage = pagerState.currentPage - 1
                            if (prevPage >= 0) {
                                pagerState.scrollToPage(prevPage)
                            }
                        }
                    },
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        } else {
            OutlinedButton(
                onClick = {
                    onInitDevice()
                    navController.popBackStack()
                    navController.navigate("Main")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Text(
                    text = "Get Started",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
private fun PageIndicator(pageCount: Int, currentPage: Int, modifier: Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        repeat(pageCount) {
            IndicatorSingleDot(isSelected = it == currentPage)
        }


    }
}

@Composable
private fun IndicatorSingleDot(isSelected: Boolean) {

    val width = animateDpAsState(targetValue = if (isSelected) 35.dp else 15.dp, label = "")
    Box(
        modifier = Modifier
            .padding(2.dp)
            .height(15.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.inversePrimary)
    )
}

@Composable
private fun SelectCountry(callback: (String) -> Unit) {
    CountryCodeDialog(
        pickedCountry = {
            callback(it.countryCode)
        },
        defaultSelectedCountry = getListOfCountries().first(),
        dialogSearch = true,
        dialogRounded = 22
    )
}

@Composable
private fun LoginRegisterBox(navController: NavHostController, onInitDevice: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedButton(
            onClick = {
                onInitDevice()
                navController.popBackStack()
                navController.navigate("Login")
            },
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Text(
                text = "Login",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        OutlinedButton(
            onClick = {
                onInitDevice()
                navController.popBackStack()
                navController.navigate("Register")
            },
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Text(
                text = "Register",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun SelectNotifications(missingSetting: Setting?, encounteredSetting: Setting?, updateSettingCallback: (Setting, String) -> Unit){
    val context = LocalContext.current
    var missingSettingSelected by remember {
        mutableStateOf("None")
    }
    var encounteredSettingSelected by remember {
        mutableStateOf("None")
    }
    val notificationPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(
            permission = Manifest.permission.POST_NOTIFICATIONS,
            onPermissionResult = {granted ->
                if(!granted){
                    missingSettingSelected = "None"
                    encounteredSettingSelected = "None"
                }
            }
        )
    } else {
        null
    }
    val backgroundLocationPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {rememberPermissionState(
        permission = Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        onPermissionResult = {granted ->
            if(granted && notificationPermissionState != null){
                notificationPermissionState.launchPermissionRequest()
            }
            else if(!granted){
                missingSettingSelected = "None"
                encounteredSettingSelected = "None"
            }
        }
    )}
    else {
        null
    }
    val locationPermissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION,
        onPermissionResult = { granted ->
            if(granted && backgroundLocationPermissionState != null){
                if(!backgroundLocationPermissionState.status.isGranted){
                    Toast.makeText(context, "You Need To Allow Location Access All The Time To Receive Notifications Even When The Application Isn't Open", Toast.LENGTH_LONG).show()
                }
                backgroundLocationPermissionState.launchPermissionRequest()
            }
            else if(granted && notificationPermissionState != null){
                notificationPermissionState.launchPermissionRequest()
            }
            else if(!granted){
                missingSettingSelected = "None"
                encounteredSettingSelected = "None"
            }
        }
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(text = "Missing Notifications")
        missingSetting?.options?.forEach {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.width(200.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.dot),
                    contentDescription = "Selection Icon",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = it)
                Spacer(modifier = Modifier.weight(1f))
                RadioButton(selected = missingSettingSelected == it, onClick = {
                    missingSettingSelected = it
                    if(it.contains("Nearby")) {
                        locationPermissionState.launchPermissionRequest()
                    }
                    updateSettingCallback(missingSetting, it)
                })
            }
        }
        Text(text = "Encountered Notifications")
        encounteredSetting?.options?.forEach {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.width(200.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.dot),
                    contentDescription = "Selection Icon",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = it)
                Spacer(modifier = Modifier.weight(1f))
                RadioButton(selected = encounteredSettingSelected == it, onClick = {
                    encounteredSettingSelected = it
                    if(it.contains("Nearby")) {
                        locationPermissionState.launchPermissionRequest()
                    }
                    updateSettingCallback(encounteredSetting, it)
                })
            }
        }
    }
}
