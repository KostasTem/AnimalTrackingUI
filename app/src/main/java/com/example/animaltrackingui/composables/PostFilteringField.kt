package com.example.animaltrackingui.composables

import android.Manifest
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.animaltrackingui.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostFiltering(
    animalList: List<String>,
    locationList: List<String>,
    sortingCallback: (String) -> Unit,
    selectedFilterCallback: (List<String>, List<String>) -> Unit,
    selectedPostTypeCallback: (String) -> Unit,
    selectedPostLocaleCallback: (String) -> Unit,
    mapFiltering: Boolean
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
    var selectedPostLocation by remember {
        mutableStateOf("My Country")
    }
    var selectedPostType by remember {
        mutableStateOf("All")
    }
    val selectedAnimals by remember {
        mutableStateOf(mutableListOf<String>())
    }
    val selectedLocations by remember {
        mutableStateOf(mutableListOf<String>())
    }
    val bottomSheetColumnScrollState = rememberScrollState()
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
                if (mapFiltering) {
                    MapPostLocationSelection(
                        selectedPostLocation = selectedPostLocation,
                        postSelectionCallback = {
                            selectedPostLocaleCallback(it)
                            selectedPostLocation = it
                        })
                    MapPostTypeSelection(
                        selectedPostType = selectedPostType,
                        postSelectionCallback = {
                            selectedPostTypeCallback(it)
                            selectedPostType = it
                        })
                } else {
                    PostSorting(selectedSort = selectedSort, callback = {
                        selectedSort = it
                        sortingCallback(it)
                    })
                }
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
    HorizontalDivider()
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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun MapPostLocationSelection(
    selectedPostLocation: String,
    postSelectionCallback: (String) -> Unit
) {
    val postSelectionVars by remember {
        mutableStateOf(listOf("Nearby", "My Country", "All"))
    }
    val locationPermissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        Text(text = "Show Posts", style = MaterialTheme.typography.titleMedium)
        postSelectionVars.forEach {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(4.dp)
            ) {
                Text(text = it, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.weight(1f))
                RadioButton(selected = selectedPostLocation == it, onClick = {
                    if (it == "Nearby" && !locationPermissionState.status.isGranted) {
                        return@RadioButton
                    }
                    postSelectionCallback(it)
                })
            }
        }
        HorizontalDivider()
    }
}

@Composable
private fun MapPostTypeSelection(
    selectedPostType: String,
    postSelectionCallback: (String) -> Unit
) {
    val postTypeVars by remember {
        mutableStateOf(listOf("All", "Missing", "Encountered"))
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        Text(text = "Post Type", style = MaterialTheme.typography.titleMedium)
        postTypeVars.forEach {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(4.dp)
            ) {
                Text(text = it, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.weight(1f))
                RadioButton(selected = selectedPostType == it, onClick = {
                    postSelectionCallback(it)
                })
            }
        }
        HorizontalDivider()
    }
}

@Composable
fun Modifier.simpleVerticalScrollbar(
    state: LazyListState,
    width: Dp = 8.dp
): Modifier {
    val targetAlpha = if (state.isScrollInProgress) 1f else 0f
    val duration = if (state.isScrollInProgress) 150 else 500

    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = duration), label = "Lazy List Scrollbar Animation"
    )

    return drawWithContent {
        drawContent()

        val firstVisibleElementIndex = state.layoutInfo.visibleItemsInfo.firstOrNull()?.index
        val needDrawScrollbar = state.isScrollInProgress || alpha > 0.0f

        // Draw scrollbar if scrolling or if the animation is still running and lazy column has content
        if (needDrawScrollbar && firstVisibleElementIndex != null) {
            val elementHeight = this.size.height / state.layoutInfo.totalItemsCount
            val scrollbarOffsetY = firstVisibleElementIndex * elementHeight
            val scrollbarHeight = state.layoutInfo.visibleItemsInfo.size * elementHeight

            drawRect(
                color = Color.Red,
                topLeft = Offset(this.size.width - width.toPx(), scrollbarOffsetY),
                size = Size(width.toPx(), scrollbarHeight),
                alpha = alpha
            )
        }
    }
}


private fun Modifier.verticalScrollDisabled() =
    pointerInput(Unit) {
        awaitPointerEventScope {
            while (true) {
                awaitPointerEvent(pass = PointerEventPass.Initial).changes.forEach {
                    val offset = it.positionChange()
                    if (abs(offset.y) > 0f) {
                        it.consume()
                    }
                }
            }
        }
    }