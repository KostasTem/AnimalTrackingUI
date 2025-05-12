package com.example.animaltrackingui.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.animaltrackingui.R

@Composable
fun AnimalIdentificationDialog(showIdentificationDialog: Boolean, identifiedAnimal: String?, dismissCallback: () -> Unit, onAnimalTypeChange: (String) -> Unit){
    if(showIdentificationDialog) {
        AlertDialog(
            onDismissRequest = { dismissCallback() },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.pet),
                    contentDescription = "Pet Icon"
                )
            },
            title = { Text(text = "Animal Identified!") },
            text = { Text(text = "This Animal Was Detected To Be: ${identifiedAnimal}.\n Do You Think This Animal Was Incorrectly Identified?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        dismissCallback()
                    }) {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onAnimalTypeChange(identifiedAnimal!!)
                    dismissCallback()
                }) {
                    Text(text = "No")
                }
            })
    }
}