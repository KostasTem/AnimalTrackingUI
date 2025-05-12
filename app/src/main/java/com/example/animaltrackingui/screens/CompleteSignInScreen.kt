package com.example.animaltrackingui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.animaltrackingui.R
import com.example.animaltrackingui.composables.CountryCodeDialog
import com.example.animaltrackingui.composables.DatePickerField
import com.example.animaltrackingui.db.getListOfCountries
import com.example.animaltrackingui.viewModels.AnimalTrackingViewModelProvider
import com.example.animaltrackingui.viewModels.CompleteFirstGoogleSignInViewModel

@Composable
fun CompleteGoogleSignInScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    completeFirstGoogleSignInViewModel: CompleteFirstGoogleSignInViewModel = viewModel(
        factory = AnimalTrackingViewModelProvider.Factory
    )
) {
    val uiState = completeFirstGoogleSignInViewModel.completeGoogleSignInViewModel.collectAsState()
    BackHandler {
        completeFirstGoogleSignInViewModel.abortOperation()
        navController.navigate("Login")
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
                    Text(
                        text = "Complete Your Registration",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold
                    )
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
                        CountryCodeDialog(
                            pickedCountry = {
                                completeFirstGoogleSignInViewModel.onCountryCodeChange(it.countryCode)
                            },
                            defaultSelectedCountry = getListOfCountries().first(),
                            dialogSearch = true,
                            dialogRounded = 22,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(75.dp)
                        )
                        DatePickerField(
                            fieldValue = uiState.value.age.toString(),
                            fieldLabel = "Date Of Birth",
                            icon = R.drawable.calendar_small,
                            onFieldValueChanged = {

                            },
                            onAgeChange = completeFirstGoogleSignInViewModel::onAgeChange
                        )
                        Button(
                            onClick = {
                                completeFirstGoogleSignInViewModel.completeRegistration(
                                    navController
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(Color.Transparent)
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(50.dp)
                                    )
                                    .fillMaxWidth()
                                    .heightIn(48.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Complete", color = Color.White, fontSize = 20.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}