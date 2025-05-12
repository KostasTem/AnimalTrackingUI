package com.example.animaltrackingui.composables

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.animaltrackingui.R
import com.example.animaltrackingui.db.Setting
import com.example.animaltrackingui.db.SettingCategory
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SettingsRadioDropdown(
    name: String,
    imageVector: ImageVector?,
    iconDesc: String?,
    setting: Setting,
    onSettingChanged: (Setting, String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val context = LocalContext.current
    var isDropdownExpanded by rememberSaveable {
        mutableStateOf(false)
    }
    val transition = updateTransition(targetState = isDropdownExpanded, label = "transition")
    val rotate by animateFloatAsState(
        targetValue = if (isDropdownExpanded) 90f else 0f,
        label = "rotate"
    )
    var selected by remember {
        mutableStateOf(setting.selected)
    }
    var rememberSelected by remember {
        mutableStateOf("")
    }
    var color by remember {
        mutableStateOf(if (enabled) Color.Black else Color.Gray)
    }
    val notificationPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(
            permission = Manifest.permission.POST_NOTIFICATIONS,
            onPermissionResult = {granted ->
                if(granted){
                    onSettingChanged(setting,rememberSelected)
                    selected = rememberSelected
                }
                rememberSelected = ""
            }
        )
    } else {
        null
    }
    val backgroundLocationPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {rememberPermissionState(
        permission = Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        onPermissionResult = {granted ->
            if(granted){
                notificationPermissionState?.launchPermissionRequest()
            }
            else{
                rememberSelected = ""
            }
        }
    )}
    else {
        null
    }
    val locationPermissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION,
        onPermissionResult = { granted ->
            if(granted && backgroundLocationPermissionState != null){
                if(!backgroundLocationPermissionState.status.isGranted){
                    Toast.makeText(context, "You Need To Allow Location Access All The Time To Receive Notifications Even When The Application Isn't Open", Toast.LENGTH_LONG).show()
                }
                backgroundLocationPermissionState.launchPermissionRequest()
            }
            else if(granted && notificationPermissionState != null){
                notificationPermissionState.launchPermissionRequest()
            }
            else if(granted){
                onSettingChanged(setting, rememberSelected)
                selected = rememberSelected
                rememberSelected = ""
            }
            else{
                rememberSelected = ""
            }
        }
    )
    LaunchedEffect(key1 = enabled) {
        if (!enabled) {
            isDropdownExpanded = false
            color = Color.Gray
        } else {
            color = Color.Black
        }
    }
    Surface(
        color = Color.Transparent,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        onClick = {
            if (enabled) {
                isDropdownExpanded = transition.currentState != true
            }
        },
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (imageVector != null) {
                        Icon(
                            imageVector = imageVector,
                            contentDescription = iconDesc,
                            modifier = Modifier.size(24.dp),
                            tint = color
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = name,
                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, start = 16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Start,
                        color = color
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
                if (!isDropdownExpanded) {
                    Text(
                        text = if (selected == "") setting.selected.split(" ")[0] else selected.split(" ")[0],
                        fontSize = 10.sp,
                        textAlign = TextAlign.End
                    )
                }
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    modifier = Modifier.rotate(rotate),
                    contentDescription = "Open Setting Icon",
                    tint = color
                )
            }
            if (transition.currentState) {
                Column {
                    setting.options.forEach {
                        RadioDropdownItem(
                            selected = if (selected == "") setting.selected else selected,
                            option = it,
                            onClick = {
                                if (setting.category == SettingCategory.Notification && (!locationPermissionState.status.isGranted || (backgroundLocationPermissionState != null && !backgroundLocationPermissionState.status.isGranted) || (notificationPermissionState != null && !notificationPermissionState.status.isGranted))) {
                                    rememberSelected = it
                                    locationPermissionState.launchPermissionRequest()
                                } else {
                                    onSettingChanged(setting, it)
                                    selected = it
                                }
                            })
                    }
                }
            }
            HorizontalDivider()
        }
    }
}

@Composable
private fun RadioDropdownItem(selected: String, option: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(40.dp))
            Icon(
                painter = painterResource(id = R.drawable.dot),
                contentDescription = "Selection Icon",
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = option,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        RadioButton(
            selected = (option == selected),
            onClick = onClick
        )
    }
}