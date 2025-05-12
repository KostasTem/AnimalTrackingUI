package com.example.animaltrackingui.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animaltrackingui.db.AppRepository
import com.example.animaltrackingui.db.Setting
import com.example.animaltrackingui.services.ToastService
import com.example.animaltrackingui.states.InitUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InitViewModel(private val appRepository: AppRepository, private val toastService: ToastService): ViewModel() {

    private val _initUiState: MutableStateFlow<InitUiState> =
        MutableStateFlow(InitUiState())
    val initUiState: StateFlow<InitUiState> = _initUiState.asStateFlow()
    private var retry = true


    init {
        viewModelScope.launch {
            _initUiState.update {
                it.copy(
                    missingNotificationSetting = appRepository.getSetting("Receive Missing Notifications"),
                    encounteredNotificationSetting = appRepository.getSetting("Receive Encountered Notifications"),
                    localizationSetting = appRepository.getSetting("Selected Country")
                )
            }
        }
    }

    fun updateSetting(setting: Setting, value: String){
        viewModelScope.launch {
            setting.selected = value
            appRepository.updateSetting(setting)
        }
    }

    fun completeInitialization(context: Context){
        viewModelScope.launch {
            if(appRepository.getDevice() != null) {
                context.applicationContext.getSharedPreferences("ATData", Context.MODE_PRIVATE)
                    .edit().putBoolean("initialized", true).apply()
                appRepository.updateDeviceInitialized(true)
            }
            else{
                if(retry) {
                    retry = false
                    appRepository.initDevice()
                    delay(1500)
                    completeInitialization(context)
                }
                else {
                    retry = true
                    toastService.displayToast("There Was An Error Setting Everything Up. Please Try Again Later!")
                }
            }
        }
    }

}