package com.example.animaltrackingui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.animaltrackingui.db.Setting
import com.example.animaltrackingui.viewModels.SettingsViewModel

@Composable
fun SettingsSwitchComp(
    name: String,
    imageVector: ImageVector,
    iconDesc: String,
    settingsViewModel: SettingsViewModel,
    switchChangedCallback: (Boolean) -> Unit
) {
    val uiState = settingsViewModel.settingsUiState.collectAsState()
    val setting = uiState.value.settings.find{ it.name == name  } ?: Setting()
    var switchState by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(setting.selected){
        switchState = setting.selected == "true"
        switchChangedCallback(switchState)
    }
    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = imageVector,
                        contentDescription = iconDesc,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = name,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Start,
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                Switch(
                    checked = switchState,
                    onCheckedChange = {
                        val state = if (switchState) "false" else "true"
                        settingsViewModel.updateSetting(setting, state)
                        switchState = switchState.not()
                        switchChangedCallback(switchState)
                    }
                )

            }
            HorizontalDivider()
        }
    }
}