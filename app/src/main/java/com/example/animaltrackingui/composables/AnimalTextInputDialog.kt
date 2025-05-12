package com.example.animaltrackingui.composables

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction

@Composable
fun AnimalTextInputDialog(showAnimalTypeInputDialog: Boolean, dismissCallback: () -> Unit, onAnimalTypeChange: (String) -> Unit){
    var animalType by remember {
        mutableStateOf("")
    }
    if (showAnimalTypeInputDialog) {
        AlertDialog(onDismissRequest = { dismissCallback() }, confirmButton = {
            TextButton(onClick = {
                onAnimalTypeChange(animalType)
                dismissCallback()
            }) {
                Text(text = "Confirm")
            }

        },
            dismissButton = {
                TextButton(onClick = { dismissCallback() }) {
                    Text(text = "Dismiss")
                }
            },
            title = { Text(text = "Animal Type") },
            text = {
                TextField(
                    value = animalType,
                    placeholder = { Text(text = "Animal") },
                    onValueChange = { animalType = it },
                    colors = TextFieldDefaults.colors(
                        focusedLabelColor = MaterialTheme.colorScheme.outline,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedLeadingIconColor = MaterialTheme.colorScheme.primary
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        onAnimalTypeChange(animalType)
                        dismissCallback()
                    })
                )
            })
    }
}

