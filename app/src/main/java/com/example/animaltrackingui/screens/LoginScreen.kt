package com.example.animaltrackingui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.animaltrackingui.R
import com.example.animaltrackingui.composables.CustomTextField
import com.example.animaltrackingui.composables.LRBottomComponent
import com.example.animaltrackingui.composables.PasswordTextField
import com.example.animaltrackingui.ui.theme.AppTheme
import com.example.animaltrackingui.viewModels.AnimalTrackingViewModelProvider
import com.example.animaltrackingui.viewModels.SignInViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    signInViewModel: SignInViewModel = viewModel(factory = AnimalTrackingViewModelProvider.Factory),
    navController: NavHostController = rememberNavController()
) {
    val signInUiState by signInViewModel.signInUiState.collectAsState()
    BackHandler {
        navController.navigate("Main")
    }
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
                    Text(text = "Welcome Back", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CustomTextField(
                        fieldValue = signInUiState.email,
                        fieldLabel = "Email",
                        icon = R.drawable.email_small,
                        keyboardType = KeyboardType.Email,
                        onFieldValueChanged = { signInViewModel.onEmailChange(it) })
                    PasswordTextField(fieldValue = signInUiState.password,
                        onFieldValueChanged = { signInViewModel.onPasswordChange(it) })
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Stay Signed In?")
                        Spacer(modifier = Modifier.width(4.dp))
                        Checkbox(
                            checked = signInUiState.staySignedIn,
                            onCheckedChange = { signInViewModel.onStaySignedInChange(it) })
                    }
                    LRBottomComponent(
                        textQuery = "Don't have an account? ",
                        textClickable = "Register",
                        action = "Login",
                        onClickAction = { signInViewModel.login(navController = navController) },
                        googleSignInClient = signInViewModel.googleSignInClient,
                        onGoogleSignIn = { signInViewModel.signInWithGoogle(navController, it) },
                        navController = navController
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun Preview() {
    AppTheme {
        LoginScreen()
    }
}