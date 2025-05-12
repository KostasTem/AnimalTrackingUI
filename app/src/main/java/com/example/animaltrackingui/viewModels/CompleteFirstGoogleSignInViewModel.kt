package com.example.animaltrackingui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.animaltrackingui.db.AppRepository
import com.example.animaltrackingui.retrofit.RetrofitObject
import com.example.animaltrackingui.states.CompleteFirstGoogleSignInUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class CompleteFirstGoogleSignInViewModel(private val appRepository: AppRepository): ViewModel() {

    private val _completeGoogleSignInViewModel = MutableStateFlow(CompleteFirstGoogleSignInUiState())
    val completeGoogleSignInViewModel: StateFlow<CompleteFirstGoogleSignInUiState> = _completeGoogleSignInViewModel.asStateFlow()

    init {
        viewModelScope.launch {
            _completeGoogleSignInViewModel.update {
                it.copy(
                    user = appRepository.getUser()
                )
            }
        }
    }

    fun onAgeChange(date: LocalDate) {
        _completeGoogleSignInViewModel.update {
            it.copy(
                age = date
            )
        }
    }

    fun onCountryCodeChange(text: String) {
        _completeGoogleSignInViewModel.update {
            it.copy(
                countryCode = text
            )
        }
    }

    fun completeRegistration(navController: NavHostController){
        viewModelScope.launch{
            val completeRegistrationResponse = RetrofitObject.userAPI.completeFirstGoogleSignIn(completeGoogleSignInViewModel.value.user!!,completeGoogleSignInViewModel.value.user!!.token)
            if(completeRegistrationResponse.isSuccessful && completeRegistrationResponse.body() != null){
                appRepository.updateUser(completeRegistrationResponse.body()!!)
                navController.navigate("Main")
            }
            else{

            }
        }
    }

    fun abortOperation(){
        viewModelScope.launch {
            appRepository.removeUser()
        }
    }

}