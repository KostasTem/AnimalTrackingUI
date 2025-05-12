package com.example.animaltrackingui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.animaltrackingui.db.AppRepository
import com.example.animaltrackingui.db.AppUser
import com.example.animaltrackingui.db.LoginRequest
import com.example.animaltrackingui.retrofit.RetrofitObject
import com.example.animaltrackingui.services.ToastService
import com.example.animaltrackingui.states.SignInUiState
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class SignInViewModel(
    private val appRepository: AppRepository,
    val googleSignInClient: GoogleSignInClient,
    private val toastService: ToastService
) : ViewModel() {

    private val _signInUiState = MutableStateFlow(SignInUiState())
    val signInUiState: StateFlow<SignInUiState> = _signInUiState.asStateFlow()


    init {
        viewModelScope.launch() {
            _signInUiState.update {
                it.copy(
                    device = appRepository.getDevice()
                )
            }
        }
    }

    fun onEmailChange(text: String) {
        _signInUiState.update {
            it.copy(
                email = text
            )
        }
    }

    fun onPasswordChange(text: String) {
        _signInUiState.update {
            it.copy(
                password = text
            )
        }
    }

    fun onStaySignedInChange(bool: Boolean) {
        _signInUiState.update {
            it.copy(
                staySignedIn = bool
            )
        }
    }

    fun signInWithGoogle(navController: NavHostController, task: Task<GoogleSignInAccount>) {
        viewModelScope.launch {
            if (task.result.idToken != null && signInUiState.value.device != null) {
                val loginResponse = RetrofitObject.userAPI.signInWithGoogle(
                    LoginRequest(
                        null,
                        null,
                        signInUiState.value.device!!.deviceID,
                        task.result.idToken!!,
                        true
                    )
                )
                if (loginResponse.isSuccessful && loginResponse.body() != null) {
                    completeLogin(loginResponse.body()!!, navController)
                } else {
                    Log.e("Login Error", loginResponse.code().toString())
                }
            }
            else{
                toastService.displayToast("There was an error during your login. Please try again later.")
                appRepository.initDevice()
            }
        }
    }

    fun login(navController: NavHostController) {
        viewModelScope.launch {
            if (signInUiState.value.device != null) {
                val loginResponse = RetrofitObject.userAPI.login(
                    LoginRequest(
                        signInUiState.value.email,
                        signInUiState.value.password,
                        signInUiState.value.device!!.deviceID,
                        null,
                        signInUiState.value.staySignedIn
                    )
                )
                if (loginResponse.isSuccessful && loginResponse.body() != null) {
                    completeLogin(loginResponse.body()!!, navController)
                } else {
                    toastService.displayToast("Invalid Credentials")
                    Log.e("Login Error", loginResponse.code().toString())
                }
            }
            else{
                toastService.displayToast("There was an error during your login. Please try again later.")
                appRepository.initDevice()
            }
        }
    }

    private suspend fun completeLogin(appUser: AppUser, navController: NavHostController) {
        Firebase.auth.signInWithCustomToken(appUser.firebaseAuthToken)
            .addOnSuccessListener {
                viewModelScope.launch {
                    appRepository.addUser(appUser)
                    if (appUser.accountVerified) {
                        navController.navigate("Main")
                    } else {
                        navController.navigate("CompleteFirstGoogleSignIn")
                    }
                    it.user?.uid?.let { it1 -> Log.i("UID", it1) }
                }
            }.addOnFailureListener {
                toastService.displayToast("There Was An Error During Your Login.")
            }
    }

}