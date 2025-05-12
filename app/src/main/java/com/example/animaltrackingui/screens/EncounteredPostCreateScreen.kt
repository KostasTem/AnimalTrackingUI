package com.example.animaltrackingui.screens

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.animaltrackingui.LocationProvider
import com.example.animaltrackingui.R
import com.example.animaltrackingui.Utils
import com.example.animaltrackingui.composables.AnimalIdentificationDialog
import com.example.animaltrackingui.composables.AnimalTextInputDialog
import com.example.animaltrackingui.composables.AnimalTypeSelectDropdown
import com.example.animaltrackingui.composables.CreatePostActionBottomSheet
import com.example.animaltrackingui.composables.CreatePostTopAppBar
import com.example.animaltrackingui.composables.CustomTextField
import com.example.animaltrackingui.composables.UploadConfirmationDialog
import com.example.animaltrackingui.db.getCountryLatLng
import com.example.animaltrackingui.states.PostType
import com.example.animaltrackingui.viewModels.AnimalTrackingViewModelProvider
import com.example.animaltrackingui.viewModels.CreatePostViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener


@SuppressLint("MissingPermission")
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class,
    ExperimentalGlideComposeApi::class, ExperimentalMaterialApi::class
)
@Composable
fun EncounteredPostCreateScreen(
    navController: NavHostController,
    createPostViewModel: CreatePostViewModel = viewModel(
        factory = AnimalTrackingViewModelProvider.Factory
    )
) {
    val context = LocalContext.current
    val uri = Utils.createImageFile(context)
    var showAnimalTypeInputDialog by remember {
        mutableStateOf(false)
    }
    val scrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val uiState = createPostViewModel.createPostUiState.collectAsState()
    createPostViewModel.onPostTypeChanged(PostType.ENCOUNTERED)
    val locationPermissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION,
        onPermissionResult = { granted ->
            if (granted) LocationProvider.provider.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                        CancellationTokenSource().token

                    override fun isCancellationRequested() = false
                }).addOnSuccessListener {
                if (it != null) {
                    createPostViewModel.onLatLngChange(LatLng(it.latitude, it.longitude))
                    createPostViewModel.onShowSelectedLocationChange(true)
                } else {
                    createPostViewModel.onLatLngChange(getCountryLatLng(uiState.value.user.countryCode)!!)
                }
            }
            else {
                createPostViewModel.onLatLngChange(getCountryLatLng(uiState.value.user.countryCode)!!)
            }

        }
    )
    LaunchedEffect(key1 = uiState.value.animalImageUri) {
        if (uiState.value.animalImageUri.toString() != "") {
            createPostViewModel.onShowSelectedImageChange(true)
        } else {
            createPostViewModel.onShowSelectedImageChange(false)
        }
    }
    LaunchedEffect(key1 = uiState.value.location) {
        if (uiState.value.location[0] != "") {
            createPostViewModel.onShowSelectedLocationChange(true)
        } else {
            createPostViewModel.onShowSelectedLocationChange(false)
        }
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CreatePostTopAppBar(
                title = "Encountered Post",
                scrollBehavior = scrollBehavior,
                navController = navController,
                actionOnClick = createPostViewModel::onOpenAlertDialogChange
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(28.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                AnimalTypeSelectDropdown(
                    animalType = uiState.value.animalType,
                    animalTypeList = uiState.value.animalTypeList,
                    onAnimalTypeChanged = { createPostViewModel.onAnimalTypeChange(it) },
                    showAnimalTypeInputDialogCallback = { showAnimalTypeInputDialog = true })
                CustomTextField(
                    fieldValue = uiState.value.text,
                    fieldLabel = "Comment",
                    icon = R.drawable.comment_small,
                    onFieldValueChanged = { createPostViewModel.onTextChange(it) }
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Hostile")
                    Spacer(modifier = Modifier.weight(1f))
                    Checkbox(
                        checked = uiState.value.hostile,
                        onCheckedChange = { createPostViewModel.onHostileChanged(it) })
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        shape = RoundedCornerShape(10.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 10.dp,
                            pressedElevation = 6.dp
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        ),
                        onClick = {
                            createPostViewModel.onSelectedBottomSheetChange(1)
                            createPostViewModel.onShowBottomSheetChange(true)
                        }) {
                        Icon(
                            painter = painterResource(id = R.drawable.image_search),
                            contentDescription = "Image Search Icon"
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(text = "Select Image")
                    }
                    Button(
                        shape = RoundedCornerShape(10.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 10.dp,
                            pressedElevation = 6.dp
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        ), onClick = {
                            createPostViewModel.onSelectedBottomSheetChange(2)
                            createPostViewModel.onShowBottomSheetChange(true)
                        }) {
                        Icon(
                            painter = painterResource(id = R.drawable.map),
                            contentDescription = "Map Icon"
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(text = "Pick Location")
                    }
                }
                if (uiState.value.showSelectedLocation) {
                    Text(
                        text = uiState.value.location.joinToString(",")
                    )
                }
                if (uiState.value.showSelectedImage) {
                    GlideImage(
                        model = if (uiState.value.animalImageUri.toString() == "") uri else uiState.value.animalImageUri,
                        contentDescription = "Encountered Animal Selected Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        contentScale = ContentScale.Fit
                    )
                }
                if (uiState.value.showBottomSheet) {
                    CreatePostActionBottomSheet(
                        uri = uri,
                        selectedBottomSheet = uiState.value.selectedBottomSheet,
                        mapCenter = LatLng(uiState.value.latitude, uiState.value.longitude),
                        locationPermissionState = locationPermissionState,
                        onImageSelect = {
                            createPostViewModel.onAnimalImageUriChange(it)
                            createPostViewModel.uploadImageForIdentification(context)
                        },
                        onShowBottomSheetChange = createPostViewModel::onShowBottomSheetChange,
                        onSelectedBottomSheetChange = createPostViewModel::onSelectedBottomSheetChange,
                        onLatLngChange = createPostViewModel::onLatLngChange
                    )
                }
            }
        })
    AnimalTextInputDialog(
        showAnimalTypeInputDialog = showAnimalTypeInputDialog,
        dismissCallback = { showAnimalTypeInputDialog = false },
        onAnimalTypeChange = createPostViewModel::onAnimalTypeChange
    )
    AnimalIdentificationDialog(
        showIdentificationDialog = uiState.value.showIdentificationDialog,
        identifiedAnimal = uiState.value.identifiedAnimal,
        dismissCallback = { createPostViewModel.onShowIdentificationDialogChange(false) },
        onAnimalTypeChange = createPostViewModel::onAnimalTypeChange
    )
    UploadConfirmationDialog(
        openAlertDialog = uiState.value.openAlertDialog,
        uploadCallback = {
            createPostViewModel.uploadEncounteredPost(
                context,
                navController
            )
        },
        dismissCallback = { createPostViewModel.onOpenAlertDialogChange(false) }
    )
}

