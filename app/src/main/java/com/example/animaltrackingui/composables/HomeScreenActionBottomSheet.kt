package com.example.animaltrackingui.composables

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.animaltrackingui.R
import com.example.animaltrackingui.Utils
import com.example.animaltrackingui.db.AppUser
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreenActionBottomSheet(
    navController: NavHostController,
    sheetState: SheetState,
    user: AppUser?,
    showBottomSheetCallback: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val uri by remember {
        mutableStateOf(Utils.createImageFile(context))
    }
    var showNotFoundDialog by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    var showIdentifiedDialog by remember {
        mutableStateOf(false)
    }
    var selectedSheet by remember {
        mutableStateOf(0)
    }
    var identifiedAnimal by remember {
        mutableStateOf("")
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { resultUri: Uri? ->
            resultUri?.let {
                scope.launch {
                    Utils.uploadImageForIdentification(
                        context = context,
                        uri = resultUri,
                        callback = { animalIdentified ->
                            if (animalIdentified != null) {
                                showIdentifiedDialog = true
                                identifiedAnimal = animalIdentified
                            } else {
                                showNotFoundDialog = true
                            }
                        })
                }
            }
        }
    )
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                scope.launch {
                    Utils.uploadImageForIdentification(
                        context = context,
                        uri = uri,
                        callback = { animalIdentified ->
                            if (animalIdentified != null) {
                                showIdentifiedDialog = true
                                identifiedAnimal = animalIdentified
                            } else {
                                showNotFoundDialog = true
                            }
                        })
                }
            }
        }
    )
    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA,
        onPermissionResult = { granted ->
            if (granted) cameraLauncher.launch(uri)
            else Toast.makeText(
                context,
                "Camera Permission Is Required To Take A Picture Of An Animal",
                Toast.LENGTH_SHORT
            ).show()
        }
    )
    BackHandler {
        if (showIdentifiedDialog) {
            showIdentifiedDialog = false
        } else {
            when (selectedSheet) {
                0 -> {
                    showBottomSheetCallback(false)
                }

                1 -> {
                    selectedSheet = 0
                }
            }
        }
    }
    ModalBottomSheet(
        onDismissRequest = {
            showBottomSheetCallback(false)
        },
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        when (selectedSheet) {
            0 -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                ) {
                    ItemGroup(name = null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .clickable {
                                    if (user != null) {
                                        navController.navigate("EncounteredPostCreate")
                                    } else {
                                        navController.navigate("Login")
                                    }
                                    scope
                                        .launch {
                                            sheetState.hide()
                                        }
                                        .invokeOnCompletion {
                                            if (!sheetState.isVisible) {
                                                showBottomSheetCallback(false)
                                            }
                                        }
                                }
                                .background(color = MaterialTheme.colorScheme.primaryContainer)
                                .padding(16.dp)
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                painter = painterResource(id = R.drawable.eye),
                                contentDescription = "Encountered Animal Icon",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Encountered",
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }
                        HorizontalDivider()
                        Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .clickable {
                                    if (user != null) {
                                        navController.navigate("MissingPostCreate")
                                    } else {
                                        navController.navigate("Login")
                                    }
                                    scope
                                        .launch {
                                            sheetState.hide()
                                        }
                                        .invokeOnCompletion {
                                            if (!sheetState.isVisible) {
                                                showBottomSheetCallback(false)
                                            }
                                        }
                                }
                                .background(color = MaterialTheme.colorScheme.primaryContainer)
                                .padding(16.dp)
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                painter = painterResource(id = R.drawable.search),
                                contentDescription = "Missing Animal Icon",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Missing",
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }
                        HorizontalDivider()
                        Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .clickable {
                                    selectedSheet = 1
                                }
                                .background(color = MaterialTheme.colorScheme.primaryContainer)
                                .padding(16.dp)
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                painter = painterResource(id = R.drawable.camera),
                                contentDescription = "Identify Animal Icon",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Identify",
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            1 -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                ) {
                    ItemGroup(name = null) {
                        Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .clickable {
                                    selectedSheet = 0
                                    cameraPermissionState.launchPermissionRequest()
                                }
                                .background(color = MaterialTheme.colorScheme.primaryContainer)
                                .padding(16.dp)
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "Take New Image",
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }
                        HorizontalDivider()
                        Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .clickable {
                                    selectedSheet = 0
                                    launcher.launch("image/*")
                                }
                                .background(color = MaterialTheme.colorScheme.primaryContainer)
                                .padding(16.dp)
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "Select From Gallery",
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

    }
    if (showIdentifiedDialog) {
        AlertDialog(
            onDismissRequest = { showIdentifiedDialog = false },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.pet),
                    contentDescription = "Pet Icon"
                )
            },
            title = { Text(text = "Animal Identified!") },
            text = { Text(text = "This Animal Was Detected To Be: $identifiedAnimal") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showIdentifiedDialog = false
                    }) {
                    Text(text = "Dismiss")
                }
            })
    }
    if(showNotFoundDialog){
        AlertDialog(
            onDismissRequest = { showNotFoundDialog = false },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.pet),
                    contentDescription = "Pet Icon"
                )
            },
            title = { Text(text = "That Was A Tough One!") },
            text = { Text(text = "Unfortunately We Couldn't Identify The Animal From The Picture You Provided.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showNotFoundDialog = false
                    }) {
                    Text(text = "Dismiss")
                }
            })
    }
}
