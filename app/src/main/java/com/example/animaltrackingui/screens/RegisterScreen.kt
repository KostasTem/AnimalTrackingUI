package com.example.animaltrackingui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.animaltrackingui.R
import com.example.animaltrackingui.composables.CountryCodeDialog
import com.example.animaltrackingui.composables.CustomTextField
import com.example.animaltrackingui.composables.DatePickerField
import com.example.animaltrackingui.composables.LRBottomComponent
import com.example.animaltrackingui.composables.PasswordTextField
import com.example.animaltrackingui.db.getListOfCountries
import com.example.animaltrackingui.viewModels.AnimalTrackingViewModelProvider
import com.example.animaltrackingui.viewModels.RegisterViewModel

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    registerViewModel: RegisterViewModel = viewModel(factory = AnimalTrackingViewModelProvider.Factory),
    navController: NavHostController = rememberNavController()
) {
    val registerUiState by registerViewModel.registerUiState.collectAsState()
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                registerViewModel.onImageUriChange(uri)
            }
        }
    )
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(
                        top = WindowInsets.statusBars
                            .asPaddingValues()
                            .calculateTopPadding()
                    )
                    .padding(28.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Column {
                    Text(text = "Register", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    Column(
                        modifier = Modifier
                            .wrapContentHeight(),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        CustomTextField(
                            fieldValue = registerUiState.username,
                            onFieldValueChanged = { registerViewModel.onUsernameChange(it) },
                            fieldLabel = "Username",
                            icon = R.drawable.account_circle_small
                        )
                        CustomTextField(
                            fieldValue = registerUiState.email,
                            onFieldValueChanged = { registerViewModel.onEmailChange(it) },
                            fieldLabel = "Email",
                            icon = R.drawable.email_small,
                            keyboardType = KeyboardType.Email
                        )
                        PasswordTextField(
                            fieldValue = registerUiState.password,
                            onFieldValueChanged = { registerViewModel.onPasswordChange(it) }
                        )
                        PasswordTextField(
                            fieldValue = registerUiState.confirmPassword,
                            onFieldValueChanged = { registerViewModel.onConfirmPasswordChange(it) },
                            fieldLabel = "Confirm Password"
                        )
                        CountryCodeDialog(
                            pickedCountry = {
                                registerViewModel.onCountryCodeChange(it.countryCode)
                            },
                            defaultSelectedCountry = getListOfCountries().first(),
                            dialogSearch = true,
                            dialogRounded = 22,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(75.dp)
                        )
                        DatePickerField(
                            fieldValue = registerUiState.age.toString(),
                            fieldLabel = "Date Of Birth",
                            icon = R.drawable.calendar_small,
                            onFieldValueChanged = {

                            },
                            onAgeChange = registerViewModel::onAgeChange
                        )
                        Button(onClick = { launcher.launch("image/*") }) {
                            Text(text = "Select Profile Image")
                        }
                        LRBottomComponent(
                            textQuery = "Already have an account? ",
                            textClickable = "Login",
                            action = "Register",
                            onClickAction = {
                                registerViewModel.register(
                                    context = context,
                                    navController = navController
                                )
                            },
                            googleSignInClient = registerViewModel.googleSignInClient,
                            onGoogleSignIn = {
                                registerViewModel.signInWithGoogle(
                                    navController,
                                    it
                                )
                            },
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}