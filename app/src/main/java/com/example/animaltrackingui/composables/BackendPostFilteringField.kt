package com.example.animaltrackingui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.animaltrackingui.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackendPostFiltering(
    animalList: List<String>,
    locationList: List<String>,
    sortingCallback: (String) -> Unit,
    selectedFilterCallback: (List<String>, List<String>) -> Unit,
) {
    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState(
        confirmValueChange = { it != SheetValue.PartiallyExpanded },
        skipPartiallyExpanded = true,
    )
    val scope = rememberCoroutineScope()
    var selectedSort by remember {
        mutableStateOf("Post Date")
    }
    val selectedAnimals by remember {
        mutableStateOf(mutableListOf<String>())
    }
    val selectedLocations by remember {
        mutableStateOf(mutableListOf<String>())
    }
    val bottomSheetColumnScrollState = rememberScrollState()
    LaunchedEffect(key1 = animalList, key2 = locationList) {
        selectedSort = "Post Date"
        sortingCallback(selectedSort)
        selectedAnimals.clear()
        selectedLocations.clear()
    }
    Button(onClick = {
        showBottomSheet = true
        scope
            .launch {
                sheetState.expand()
            }
    }) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_tune_24),
            contentDescription = "Filter Icon"
        )
        Text(text = "Filter")
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState,
            dragHandle = null,
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(8.dp)
                .verticalScroll(bottomSheetColumnScrollState)
                .pointerInput(Unit) {
                    detectDragGestures { _, dragAmount ->
                        scope.launch {
                            bottomSheetColumnScrollState.scrollBy(-dragAmount.y)
                        }
                    }
                }) {
                Text(
                    text = "Filters",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                PostSorting(selectedSort = selectedSort, callback = {
                    selectedSort = it
                    sortingCallback(it)
                })

                MultiFilterLists(
                    animalList = animalList,
                    locationList = locationList,
                    selectedAnimals = selectedAnimals,
                    selectedLocations = selectedLocations,
                    callback = {
                        selectedFilterCallback(selectedAnimals, selectedLocations)
                        sortingCallback(selectedSort)
                    })
            }
        }
    }
}

@Composable
private fun PostSorting(selectedSort: String, callback: (String) -> Unit) {
    val sortingVars by remember {
        mutableStateOf(listOf("Post Date", "Post Animal", "Post Location"))
    }
    Text(text = "Sort By", style = MaterialTheme.typography.titleMedium)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .padding(2.dp)
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        sortingVars.forEach {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(4.dp)
            ) {
                Text(text = it, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.weight(1f))
                RadioButton(selected = selectedSort == it, onClick = {
                    callback(it)
                })
            }
        }
    }
}

@Composable
private fun MultiFilterLists(
    animalList: List<String>,
    locationList: List<String>,
    selectedAnimals: MutableList<String>,
    selectedLocations: MutableList<String>,
    callback: () -> Unit
) {
    val localSelectedAnimals = remember {
        mutableStateListOf<String>()
    }
    val localSelectedLocations = remember {
        mutableStateListOf<String>()
    }
    localSelectedAnimals.addAll(selectedAnimals)
    localSelectedLocations.addAll(selectedLocations)
    if (animalList.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            Text(
                text = "Animal",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(5.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                animalList.sortedBy { it }.forEach {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(text = it)
                        Spacer(modifier = Modifier.weight(1f))
                        Checkbox(
                            checked = localSelectedAnimals.contains(it),
                            onCheckedChange = { checked ->
                                if (checked) {
                                    selectedAnimals.add(it)
                                    localSelectedAnimals.add(it)
                                } else {
                                    selectedAnimals.remove(it)
                                    localSelectedAnimals.remove(it)
                                }
                                callback()
                            })
                    }
                }
            }
        }
        HorizontalDivider()
    }
    if (locationList.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            Text(
                text = "City",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(5.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                locationList.sortedBy { it }.forEach {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(text = it)
                        Spacer(modifier = Modifier.weight(1f))
                        Checkbox(
                            checked = localSelectedLocations.contains(it),
                            onCheckedChange = { checked ->
                                if (checked) {
                                    selectedLocations.add(it)
                                    localSelectedLocations.add(it)
                                } else {
                                    selectedLocations.remove(it)
                                    localSelectedLocations.remove(it)
                                }
                                callback()
                            })
                    }
                }
            }
        }
        HorizontalDivider()
    }
}