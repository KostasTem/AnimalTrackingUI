package com.example.animaltrackingui.composables

import android.Manifest
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CreatePostActionBottomSheet(
    uri: Uri,
    selectedBottomSheet: Int,
    mapCenter: LatLng,
    locationPermissionState: PermissionState,
    onImageSelect: (Uri) -> Unit,
    onShowBottomSheetChange: (Boolean) -> Unit,
    onSelectedBottomSheetChange: (Int) -> Unit,
    onLatLngChange: (LatLng) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        confirmValueChange = { it != SheetValue.PartiallyExpanded },
        skipPartiallyExpanded = true,
    )
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { resultUri: Uri? ->
            resultUri?.let {
                onImageSelect(resultUri)
            }
            scope.launch {
                sheetState.hide()
            }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    onShowBottomSheetChange(false)
                }
            }
        }
    )
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                onImageSelect(uri)
            }
            scope.launch {
                sheetState.hide()
            }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    onShowBottomSheetChange(false)
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
    ModalBottomSheet(
        onDismissRequest = {
            onShowBottomSheetChange(false)
        },
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            when (selectedBottomSheet) {
                1 -> {
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
                                        launcher.launch("image/*")
                                    }
                                    .background(color = MaterialTheme.colorScheme.primaryContainer)
                                    .padding(16.dp)
                            ) {
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = "Select Image From Gallery",
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(modifier = Modifier.weight(1f))
                            }
                            HorizontalDivider()
                            Row(verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .clickable {
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
                        }
                    }
                }

                2 -> {
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
                                        scope
                                            .launch {
                                                sheetState.expand()
                                            }
                                            .invokeOnCompletion {
                                                if (sheetState.currentValue == SheetValue.Expanded) {
                                                    onSelectedBottomSheetChange(3)
                                                }
                                            }
                                    }
                                    .background(color = MaterialTheme.colorScheme.primaryContainer)
                                    .padding(16.dp)
                            ) {
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = "Pick Location From Map",
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(modifier = Modifier.weight(1f))
                            }
                            HorizontalDivider()
                            Row(verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .clickable {
                                        locationPermissionState.launchPermissionRequest()
                                        scope
                                            .launch {
                                                sheetState.hide()
                                            }
                                            .invokeOnCompletion {
                                                if (!sheetState.isVisible) {
                                                    onShowBottomSheetChange(false)
                                                }
                                            }
                                    }
                                    .background(color = MaterialTheme.colorScheme.primaryContainer)
                                    .padding(16.dp)
                            ) {
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = "Use My Current Location",
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                3 -> {
                    LocationSelectionMap(
                        mapCenter = mapCenter,
                        onCompleted = {
                            onLatLngChange(it)
                            onShowBottomSheetChange(false)
                        })
                }
            }
        }
    }
}