package com.example.animaltrackingui.viewModels

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.animaltrackingui.Utils
import com.example.animaltrackingui.db.AppRepository
import com.example.animaltrackingui.db.LoginRequest
import com.example.animaltrackingui.db.RegisterRequest
import com.example.animaltrackingui.retrofit.RetrofitObject
import com.example.animaltrackingui.services.ToastService
import com.example.animaltrackingui.states.RegisterUiState
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
import java.time.LocalDate

class RegisterViewModel(private val appRepository: AppRepository, val googleSignInClient: GoogleSignInClient, private val toastService: ToastService) : ViewModel() {

    private val _registerUiState = MutableStateFlow(RegisterUiState())
    val registerUiState: StateFlow<RegisterUiState> = _registerUiState.asStateFlow()

    init {
        viewModelScope.launch() {
            _registerUiState.update {
                it.copy(
                    device = appRepository.getDevice()
                )
            }
        }
    }


    fun onEmailChange(text: String) {
        _registerUiState.update {
            it.copy(
                email = text
            )
        }
    }

    fun onUsernameChange(text: String) {
        _registerUiState.update {
            it.copy(
                username = text
            )
        }
    }

    fun onPasswordChange(text: String) {
        _registerUiState.update {
            it.copy(
                password = text
            )
        }
    }

    fun onConfirmPasswordChange(text: String) {
        _registerUiState.update {
            it.copy(
                confirmPassword = text
            )
        }
    }

    fun onImageUriChange(uri: Uri) {
        _registerUiState.update {
            it.copy(
                imageUri = uri
            )
        }
    }

    fun onAgeChange(date: LocalDate) {
        _registerUiState.update {
            it.copy(
                age = date
            )
        }
    }

    fun onCountryCodeChange(text: String) {
        _registerUiState.update {
            it.copy(
                countryCode = text
            )
        }
    }

    fun signInWithGoogle(navController: NavHostController, task: Task<GoogleSignInAccount>){
        viewModelScope.launch {
            if(task.result.idToken != null && registerUiState.value.device != null) {
                val loginResponse = RetrofitObject.userAPI.signInWithGoogle(LoginRequest(null,null,registerUiState.value.device!!.deviceID,task.result.idToken!!, true))
                if (loginResponse.isSuccessful && loginResponse.body() != null) {
                    Firebase.auth.signInWithCustomToken(loginResponse.body()!!.firebaseAuthToken)
                        .addOnSuccessListener {
                            this.launch {
                                appRepository.addUser(loginResponse.body()!!)
                                if (loginResponse.body()!!.accountVerified) {
                                    navController.navigate("Main")
                                } else {
                                    navController.navigate("CompleteFirstGoogleSignIn")
                                }
                                it.user?.uid?.let { it1 -> Log.i("UID", it1) }
                            }
                        }.addOnFailureListener {
                            toastService.displayToast("There Was An Error During Your Login.")
                        }
                } else {
                    Log.e("Login Error",loginResponse.code().toString())
                }
            }
            else{
                toastService.displayToast("There was an error during your login. Please try again later.")
                appRepository.initDevice()
            }
        }
    }

    fun register(context: Context, navController: NavHostController) {
        viewModelScope.launch {
            if (registerUiState.value.password != registerUiState.value.confirmPassword) {
                Toast.makeText(context, "Passwords Don't Match", Toast.LENGTH_SHORT).show()
                return@launch
            }
            if (LocalDate.now().minusYears(13).isBefore(registerUiState.value.age)) {
                Toast.makeText(
                    context,
                    "You must be over 13 years old to create an account",
                    Toast.LENGTH_SHORT
                ).show()
                return@launch
            }
            val uploadResult = Utils.uploadImageToFirebase(registerUiState.value.imageUri,"profile_images",registerUiState.value.email,context)
            val registrationResponse = RetrofitObject.userAPI.register(
                RegisterRequest(
                    registerUiState.value.username,
                    registerUiState.value.email,
                    registerUiState.value.password,
                    if(registerUiState.value.imageUri.toString() != "") uploadResult.first else "",
                    registerUiState.value.age.toString(),
                    registerUiState.value.countryCode.uppercase()
                )
            )
            if (registrationResponse.isSuccessful && registrationResponse.body() != null) {
                Toast.makeText(
                    context,
                    "Registration Successful",
                    Toast.LENGTH_SHORT
                ).show()
                appRepository.updateDeviceLocale(registerUiState.value.countryCode)
                navController.navigate("Login")
            } else {
                Toast.makeText(
                    context,
                    "Registration Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

