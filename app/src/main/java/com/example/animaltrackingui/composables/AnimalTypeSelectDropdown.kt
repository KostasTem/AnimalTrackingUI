package com.example.animaltrackingui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.animaltrackingui.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalTypeSelectDropdown(
    animalType: String,
    animalTypeList: List<String>,
    onAnimalTypeChanged: (String) -> Unit,
    showAnimalTypeInputDialogCallback: () -> Unit
) {
    var searchValue by remember {
        mutableStateOf("")
    }
    var animalTypesExpanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(expanded = animalTypesExpanded, onExpandedChange = {
        animalTypesExpanded = !animalTypesExpanded
    }, modifier = Modifier.background(Color.Transparent)) {
        CustomTextField(
            readOnly = true,
            fieldValue = animalType,
            onFieldValueChanged = { },
            fieldLabel = "Animal Type",
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = animalTypesExpanded
                )
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            icon = R.drawable.pet,
            modifier = Modifier.menuAnchor()
        )
        DropdownMenu(
            expanded = animalTypesExpanded,
            onDismissRequest = {
                animalTypesExpanded = false
                searchValue = ""
            },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .border(2.dp, color = MaterialTheme.colorScheme.primary)
                .exposedDropdownSize()
        ) {
            DropdownMenuItem(text = {
                MyCustomTextField(
                    value = searchValue,
                    onValueChange = { searchValue = it },
                    hint = "Search",
                    modifier = Modifier.height(48.dp)
                )
            }, onClick = { /*TODO*/ })
            animalTypeList.filter { animal ->
                if (searchValue != "") animal.lowercase()
                    .contains(searchValue.lowercase()) else true
            }.forEach {
                DropdownMenuItem(onClick = {
                    if (it != "Loading Animals..." && it != "Other") {
                        onAnimalTypeChanged(it)
                    }
                    animalTypesExpanded = false
                    searchValue = ""
                }, text = {
                    Column {
                        Text(text = it)
                        HorizontalDivider()
                    }
                })
            }
            DropdownMenuItem(text = {
                Text(text = "Other")
            }, onClick = {
                showAnimalTypeInputDialogCallback()
                animalTypesExpanded = false
            })
        }
    }
}