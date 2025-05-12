package com.example.animaltrackingui.composables

import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UploadConfirmationDialog(openAlertDialog: Boolean, uploadCallback: () -> Unit, dismissCallback:() -> Unit){
    var loading by remember {
        mutableStateOf(false)
    }
    if(openAlertDialog) {
        AlertDialog(
            icon = {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(32.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Example Icon"
                    )
                }
            },
            title = {
                Text(text = if (loading) "Uploading Post" else "Upload Post")
            },
            text = {
                if (!loading) Text(text = "Are you sure you want to upload this post?")
            },
            onDismissRequest = {
                if (!loading) {
                    dismissCallback()
                }
            },
            confirmButton = {
                if (!loading) TextButton(
                    onClick = {
                        loading = true
                        uploadCallback()
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                if (!loading) TextButton(
                    onClick = dismissCallback

                ) {
                    Text("Dismiss")
                }
            }
        )
    }
}