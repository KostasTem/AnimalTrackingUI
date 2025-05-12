package com.example.animaltrackingui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animaltrackingui.db.AppRepository
import com.example.animaltrackingui.db.Setting
import com.example.animaltrackingui.states.SettingsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(private val appRepository: AppRepository) : ViewModel() {

    private val _settingsUiState: MutableStateFlow<SettingsUiState> =
        MutableStateFlow(SettingsUiState())
    val settingsUiState: StateFlow<SettingsUiState> = _settingsUiState.asStateFlow()

    init {
        viewModelScope.launch {
            val settings = appRepository.getSettings().orEmpty()
            _settingsUiState.update {
                it.copy(
                    settings = settings
                )
            }
        }
    }

    fun updateSetting(setting: Setting, value: String) {
        viewModelScope.launch {
            setting.selected = value
            appRepository.updateSetting(setting = setting)
            _settingsUiState.update {
                it.copy(
                    settings = appRepository.getSettings().orEmpty()
                )
            }
        }
    }
}