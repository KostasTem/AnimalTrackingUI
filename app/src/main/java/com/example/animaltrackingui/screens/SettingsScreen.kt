package com.example.animaltrackingui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.animaltrackingui.composables.CountryCodeDialog
import com.example.animaltrackingui.composables.ItemGroup
import com.example.animaltrackingui.composables.SettingsDropdownSelect
import com.example.animaltrackingui.composables.SettingsRadioDropdown
import com.example.animaltrackingui.composables.SettingsSwitchComp
import com.example.animaltrackingui.db.Setting
import com.example.animaltrackingui.db.getListOfCountries
import com.example.animaltrackingui.viewModels.AnimalTrackingViewModelProvider
import com.example.animaltrackingui.viewModels.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    settingsViewModel: SettingsViewModel = viewModel(factory = AnimalTrackingViewModelProvider.Factory)
) {
    val uiState = settingsViewModel.settingsUiState.collectAsState()
    var notificationsEnabled by remember {
        mutableStateOf(true)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleMedium
                    )
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
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(it)
                .padding(16.dp)
        ) {
            ItemGroup(name = "Notifications") {
                SettingsSwitchComp(
                    name = "Receive Notifications",
                    imageVector = Icons.Outlined.Notifications,
                    iconDesc = "Icon",
                    settingsViewModel = settingsViewModel,
                    switchChangedCallback = {switchState ->
                        notificationsEnabled = switchState
                    }
                )
                SettingsRadioDropdown(
                    name = "Receive Missing Notifications",
                    imageVector = Icons.Outlined.Notifications,
                    iconDesc = "Icon",
                    setting = uiState.value.settings.find { setting -> setting.name == "Receive Missing Notifications" }
                        ?: Setting(),
                    onSettingChanged = settingsViewModel::updateSetting,
                    enabled = notificationsEnabled
                )
                SettingsRadioDropdown(
                    name = "Receive Encountered Notifications",
                    imageVector = Icons.Outlined.Notifications,
                    iconDesc = "Icon",
                    setting = uiState.value.settings.find { setting -> setting.name == "Receive Encountered Notifications" }
                        ?: Setting(),
                    onSettingChanged = settingsViewModel::updateSetting,
                    enabled = notificationsEnabled
                )
            }
            ItemGroup(name = "Localization") {
                SettingsDropdownSelect(
                    name = "Selected Country",
                    imageVector = Icons.Outlined.LocationOn,
                    iconDesc = "Selected Country Icon",
                    setting = uiState.value.settings.find { setting -> setting.name == "Selected Country" }
                        ?: Setting(),
                    content = {
                        CountryCodeDialog(
                            pickedCountry = { country ->
                                settingsViewModel.updateSetting(it, country.countryCode)
                            },
                            isOnlyFlagShow = true,
                            defaultSelectedCountry = getListOfCountries().find { country -> country.countryCode == it.selected }
                                ?: getListOfCountries().first()
                        )
                    }
                )
            }
        }
    }
}