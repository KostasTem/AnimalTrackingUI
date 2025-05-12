package com.example.animaltrackingui.composables

import android.view.KeyEvent.ACTION_DOWN
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    fieldValue: String,
    fieldLabel: String,
    icon: Int? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    onFieldValueChanged: (String) -> Unit,
    readOnly: Boolean = false,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        focusedLabelColor = MaterialTheme.colorScheme.outline,
        cursorColor = MaterialTheme.colorScheme.primary,
        focusedLeadingIconColor = MaterialTheme.colorScheme.primary
    ),
    trailingIcon: (@Composable () -> Unit)? = null
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        label = {
            Text(text = fieldLabel)
        },
        value = fieldValue,
        onValueChange = onFieldValueChanged,
        colors = colors,
        textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .onPreviewKeyEvent {
                if (it.key == Key.Tab && it.nativeKeyEvent.action == ACTION_DOWN) {
                    focusManager.moveFocus(FocusDirection.Down)
                    true
                } else {
                    false
                }
            },
        shape = MaterialTheme.shapes.medium,
        leadingIcon = {
            if (icon != null) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = "profile"
                )
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = keyboardType),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        readOnly = readOnly,
        trailingIcon = trailingIcon
    )
}