package com.example.animaltrackingui.composables

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.animaltrackingui.LocationProvider
import com.example.animaltrackingui.R
import com.example.animaltrackingui.db.EncounteredPost
import com.example.animaltrackingui.db.MissingPost
import com.example.animaltrackingui.viewModels.PostLocale
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationSelectionMap(
    mapCenter: LatLng,
    onCompleted: (LatLng) -> Unit
) {
    val scope = rememberCoroutineScope()
    val locationPermissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
    )
    val uiSettings by remember {
        mutableStateOf(MapUiSettings(zoomGesturesEnabled = true, zoomControlsEnabled = false))
    }
    val mapProperties by remember {
        mutableStateOf(MapProperties(isMyLocationEnabled = locationPermissionState.status.isGranted))
    }
    var loc by remember {
        mutableStateOf(Location(""))
    }
    if (locationPermissionState.status.isGranted) {
        scope.launch {
            LocationProvider.provider.lastLocation.addOnSuccessListener { if (it != null) loc = it }
        }
    }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(mapCenter, 5f)
    }
    LaunchedEffect(key1 = loc) {
        if (loc.hasAccuracy()) {
            cameraPositionState.move(CameraUpdateFactory.newCameraPosition(
                CameraPosition(LatLng(
                    loc.latitude,
                    loc.longitude
                ),8f, 0f, 0f)
            ))
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            modifier = Modifier.pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    cameraPositionState.move(
                        CameraUpdateFactory.scrollBy(
                            -dragAmount.x,
                            -dragAmount.y
                        )
                    )
                }
            },
            uiSettings = uiSettings
        )
        Button(
            onClick = { onCompleted(cameraPositionState.position.target) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
        ) {
            Text(text = "Select Location")
        }
        Icon(
            painter = painterResource(id = R.drawable.map_marker),
            contentDescription = "Location Icon",
            modifier = Modifier
                .padding(bottom = 36.dp)
                .align(Alignment.Center),
            tint = MaterialTheme.colorScheme.scrim
        )

    }
}

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PostMap(
    mapCenter: LatLng,
    countryCode: String,
    navController: NavHostController,
    fetchPosts: suspend (PostLocale) -> Pair<List<MissingPost>, List<EncounteredPost>>,
) {
    val scope = rememberCoroutineScope()
    var postLocaleSelection by remember {
        mutableStateOf("My Country")
    }
    var postTypeSelection by remember {
        mutableStateOf("All")
    }
    val locationPermissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
    )
    val uiSettings by remember {
        mutableStateOf(MapUiSettings(zoomGesturesEnabled = true, zoomControlsEnabled = false))
    }
    val mapProperties by remember {
        mutableStateOf(MapProperties(isMyLocationEnabled = locationPermissionState.status.isGranted))
    }
    var missingPosts by remember {
        mutableStateOf(listOf<MissingPost>())
    }
    var encounteredPosts by remember {
        mutableStateOf(listOf<EncounteredPost>())
    }
    var displayedMissingPosts by remember {
        mutableStateOf(listOf<MissingPost>())
    }
    var displayedEncounteredPosts by remember {
        mutableStateOf(listOf<EncounteredPost>())
    }
    var loc by remember {
        mutableStateOf(Location(""))
    }
    if (locationPermissionState.status.isGranted) {
        scope.launch {
            LocationProvider.provider.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken(){
                override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

                override fun isCancellationRequested() = false
            }).addOnSuccessListener { if (it != null) loc = it }
        }
    }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(mapCenter, 5f)
    }

    LaunchedEffect(key1 = loc) {
        if (loc.hasAccuracy()) {
            cameraPositionState.move(CameraUpdateFactory.newCameraPosition(
                CameraPosition(LatLng(
                    loc.latitude,
                    loc.longitude
                ),8f, 0f, 0f)
            ))
        }
    }
    LaunchedEffect(key1 = true) {
        val pair: Pair<List<MissingPost>, List<EncounteredPost>> =
            if (postLocaleSelection == "All") {
                fetchPosts(PostLocale.All)
            } else {
                fetchPosts(PostLocale.SameCountry)
            }
        missingPosts = pair.first
        displayedMissingPosts = pair.first
        encounteredPosts = pair.second
        displayedEncounteredPosts = pair.second

    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = uiSettings
        ) {
            if (postTypeSelection != "Encountered") {
                displayedMissingPosts.forEach { post ->
                    if (locationPermissionState.status.isGranted && postLocaleSelection == "Nearby") {
                        val results = FloatArray(1)
                        Location.distanceBetween(
                            post.latitude,
                            post.longitude,
                            loc.latitude,
                            loc.longitude,
                            results
                        )

                        if (results[0] > 5000) {
                            return@forEach
                        }
                    } else if (postLocaleSelection == "My Country") {
                        if (post.countryCode != countryCode) {
                            return@forEach
                        }
                    }
                    Marker(
                        state = MarkerState(position = LatLng(post.latitude, post.longitude)),
                        title = "${post.animalName}, ${post.animalAge}",
                        snippet = "Posted By ${post.username} On ${
                            LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(post.timestamp!!),
                                ZoneId.systemDefault()
                            ).format(
                                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                            )
                        }",
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
                        onInfoWindowClick = {
                            Log.i("Info Window Clicked",post.id.toString())
                            scope.launch {
                                navController.navigate("MissingPost/${post.id}")
                            }
                        }
                    )
                }
            }
            if (postTypeSelection != "Missing") {
                displayedEncounteredPosts.forEach { post ->
                    if (locationPermissionState.status.isGranted && postLocaleSelection == "Nearby") {
                        val results = FloatArray(1)
                        Location.distanceBetween(
                            post.latitude,
                            post.longitude,
                            loc.latitude,
                            loc.longitude,
                            results
                        )
                        if (results[0] > 5000) {
                            return@forEach
                        }
                    } else if (postLocaleSelection == "My Country") {
                        if (post.countryCode != countryCode) {
                            return@forEach
                        }
                    }
                    Marker(
                        state = MarkerState(position = LatLng(post.latitude, post.longitude)),
                        title = post.animalType,
                        snippet = "Posted By ${post.username} On ${
                            LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(post.timestamp!!),
                                ZoneId.systemDefault()
                            ).format(
                                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                            )
                        }",
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN),
                        onInfoWindowClick = {
                            Log.i("Info Window Clicked",post.id.toString())
                            scope.launch {
                                navController.navigate("EncounteredPost/${post.id}")
                            }
                        }
                    )
                }
            }
        }
        Column(modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 16.dp, end = 16.dp)) {
            var animalList by remember {
                mutableStateOf(missingPosts.map { post -> post.animalType } + encounteredPosts.map { post -> post.animalType })
            }
            var locationList by remember {
                mutableStateOf(missingPosts.map { post -> post.location[2] } + encounteredPosts.map { post -> post.location[2] })
            }
            LaunchedEffect(key1 = postLocaleSelection, key2 = postTypeSelection) {
                val posts = if (postLocaleSelection == "All") {
                    fetchPosts(PostLocale.All)
                } else {
                    fetchPosts(PostLocale.SameCountry)
                }
                missingPosts = posts.first
                displayedMissingPosts = posts.first
                encounteredPosts = posts.second
                displayedEncounteredPosts = posts.second
                when (postTypeSelection) {
                    "All" -> {
                        animalList =
                            missingPosts.map { post -> post.animalType } + encounteredPosts.map { post -> post.animalType }
                        locationList =
                            missingPosts.map { post -> post.location[2] } + encounteredPosts.map { post -> post.location[2] }
                    }

                    "Missing" -> {
                        animalList = missingPosts.map { post -> post.animalType }
                        locationList = missingPosts.map { post -> post.location[2] }
                    }

                    "Encountered" -> {
                        animalList = encounteredPosts.map { post -> post.animalType }
                        locationList = encounteredPosts.map { post -> post.location[2] }
                    }
                }
            }
            PostFiltering(
                animalList = animalList.distinct(),
                locationList = locationList.distinct(),
                sortingCallback = {},
                selectedFilterCallback = { animals, locations ->
                    displayedMissingPosts = missingPosts.filter { post ->
                        (if (animals.isNotEmpty()) animals.contains(post.animalType) else true) && (if (locations.isNotEmpty()) locations.contains(
                            post.location[2]
                        ) else true)
                    }
                    displayedEncounteredPosts = encounteredPosts.filter { post ->
                        (if (animals.isNotEmpty()) animals.contains(post.animalType) else true) && (if (locations.isNotEmpty()) locations.contains(
                            post.location[2]
                        ) else true)
                    }

                },
                selectedPostLocaleCallback = { postLocaleSelection = it },
                selectedPostTypeCallback = { postTypeSelection = it },
                mapFiltering = true
            )
        }
    }
}