package com.example.animaltrackingui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animaltrackingui.db.AppRepository
import com.example.animaltrackingui.db.EncounteredPost
import com.example.animaltrackingui.db.MissingPost
import com.example.animaltrackingui.retrofit.RetrofitObject
import com.example.animaltrackingui.services.ToastService
import com.example.animaltrackingui.states.HomeScreenUiState
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeScreenViewModel(
    private val appRepository: AppRepository,
    private val googleSignInClient: GoogleSignInClient,
    private val toastService: ToastService
) : ViewModel() {

    private val _homeScreenUiState: MutableStateFlow<HomeScreenUiState> =
        MutableStateFlow(HomeScreenUiState())
    val homeScreenUiState: StateFlow<HomeScreenUiState> = _homeScreenUiState.asStateFlow()

    init {
        viewModelScope.launch {
            val user = appRepository.getUser()
            _homeScreenUiState.update {
                it.copy(
                    user = user,
                    device = appRepository.getDevice()
                )
            }
        }.invokeOnCompletion {
            if (homeScreenUiState.value.user == null || homeScreenUiState.value.user?.accountVerified!!) {
                getMissingPosts()
                sortingCallback("Post Date")
            } else {
                _homeScreenUiState.update {
                    it.copy(
                        completeAccountInit = true
                    )
                }
            }
        }
    }

    suspend fun fetchPosts(locale: PostLocale): Pair<List<MissingPost>, List<EncounteredPost>> {
        var missingPosts = listOf<MissingPost>()
        var encounteredPosts = listOf<EncounteredPost>()
        when (locale) {
            PostLocale.SameCountry -> {
                val missingPostsResponse =
                    RetrofitObject.missingPostAPI.getLocalMissingPosts(
                        homeScreenUiState.value.device?.countryCode!!,
                        listOf(),
                        listOf(),
                        null
                    )
                if (missingPostsResponse.isSuccessful && missingPostsResponse.body() != null) {
                    missingPosts = missingPostsResponse.body().orEmpty()
                }
                val encounteredPostsResponse =
                    RetrofitObject.encounteredPostAPI.getLocalEncounteredPosts(
                        homeScreenUiState.value.device?.countryCode!!,
                        listOf(),
                        listOf(),
                        null
                    )
                if (encounteredPostsResponse.isSuccessful && encounteredPostsResponse.body() != null) {
                    encounteredPosts = encounteredPostsResponse.body().orEmpty()
                }
            }

            PostLocale.All -> {
                val missingPostsResponse =
                    RetrofitObject.missingPostAPI.getAllMissingPosts(
                        locations = listOf(),
                        animals = listOf(),
                        auth = null
                    )
                if (missingPostsResponse.isSuccessful && missingPostsResponse.body() != null) {
                    missingPosts = missingPostsResponse.body()!!
                }
                val encounteredPostsResponse =
                    RetrofitObject.encounteredPostAPI.getAllEncounteredPosts(
                        locations = listOf(),
                        animals = listOf(),
                        auth = null
                    )
                if (encounteredPostsResponse.isSuccessful && encounteredPostsResponse.body() != null) {
                    encounteredPosts = encounteredPostsResponse.body()!!
                }
            }
        }
        return Pair(missingPosts, encounteredPosts)
    }

    fun updatePeriodIndex(index: Int) {
        if (homeScreenUiState.value.periodIndex != index) {
            when (index) {
                0 -> {
                    getMissingPosts()
                }

                1 -> {
                    getEncounteredPosts()
                }
            }
            viewModelScope.launch {
                _homeScreenUiState.update {
                    it.copy(
                        periodIndex = index
                    )
                }
            }
        }
    }

    fun getPosts(defaultParams: Boolean = false) {
        viewModelScope.launch {
            if(defaultParams){
                _homeScreenUiState.update {
                    it.copy(
                        availableAnimals = listOf(),
                        availableLocations = listOf()
                    )
                }
            }
            when (homeScreenUiState.value.periodIndex) {
                0 -> {
                    getMissingPosts()
                }

                1 -> {
                    getEncounteredPosts()
                }
            }
        }
    }

    fun selectedFilterCallback(animals: List<String>, locations: List<String>) {
        viewModelScope.launch {
            when (homeScreenUiState.value.periodIndex) {
                0 -> {
                    getMissingPosts(locations, animals)
                }

                1 -> {
                    getEncounteredPosts(locations, animals)
                }
            }
        }
    }

    fun updateDevice(){
        viewModelScope.launch {
            _homeScreenUiState.update {
                it.copy(
                    device = appRepository.getDevice()
                )
            }
        }.invokeOnCompletion {
            getPosts(defaultParams = true)
        }
    }

    fun onShowUserPostsOnlyChanged(showUserPostsOnly: Boolean){
        viewModelScope.launch {
            _homeScreenUiState.update {
                it.copy(
                    showUserPostsOnly = showUserPostsOnly
                )
            }
        }.invokeOnCompletion {
            getPosts(defaultParams = true)
        }
    }

    fun sortingCallback(sortingValue: String) {
        when (homeScreenUiState.value.periodIndex) {
            0 -> {
                when (sortingValue) {
                    "Post Date" -> {
                        _homeScreenUiState.update {
                            it.copy(
                                missingPosts = homeScreenUiState.value.missingPosts.sortedByDescending { post -> post.timestamp }
                            )
                        }
                    }

                    "Post Animal" -> {
                        _homeScreenUiState.update {
                            it.copy(
                                missingPosts = homeScreenUiState.value.missingPosts
                                    .groupBy { post -> post.animalType }
                                    .toSortedMap(compareBy { key -> key }).values
                                    .flatten()
                            )
                        }
                    }

                    "Post Location" -> {
                        _homeScreenUiState.update {
                            it.copy(
                                missingPosts = homeScreenUiState.value.missingPosts
                                    .groupBy { post -> post.location[2] }
                                    .toSortedMap(compareBy { key -> key }).values
                                    .flatten()
                            )
                        }
                    }
                }
            }

            1 -> {
                when (sortingValue) {
                    "Post Date" -> {
                        _homeScreenUiState.update {
                            it.copy(
                                encounteredPosts = homeScreenUiState.value.encounteredPosts.sortedByDescending { post -> post.timestamp }
                            )
                        }
                    }

                    "Post Animal" -> {
                        _homeScreenUiState.update {
                            it.copy(
                                encounteredPosts = homeScreenUiState.value.encounteredPosts
                                    .groupBy { post -> post.animalType }
                                    .toSortedMap(compareBy { key -> key }).values
                                    .flatten()
                            )
                        }
                    }

                    "Post Location" -> {
                        _homeScreenUiState.update {
                            it.copy(
                                encounteredPosts = homeScreenUiState.value.encounteredPosts
                                    .groupBy { post -> post.location[2] }
                                    .toSortedMap(compareBy { key -> key }).values
                                    .flatten()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getMissingPosts(
        locations: List<String> = listOf(),
        animals: List<String> = listOf()
    ) {
        viewModelScope.launch {
            _homeScreenUiState.update {
                it.copy(
                    refreshing = true
                )
            }
            val missingPostsResponse = RetrofitObject.missingPostAPI.getLocalMissingPosts(
                countryCode = homeScreenUiState.value.device?.countryCode!!,
                locations = locations,
                animals = animals,
                auth = if(homeScreenUiState.value.showUserPostsOnly) homeScreenUiState.value.user!!.token else null
            )
            if(missingPostsResponse.isSuccessful && missingPostsResponse.body() != null) {
                if (locations.isEmpty() && animals.isEmpty()) {
                    _homeScreenUiState.update {
                        it.copy(
                            missingPosts = missingPostsResponse.body().orEmpty(),
                            availableAnimals = missingPostsResponse.body()!!.map { it.animalType }
                                .distinct(),
                            availableLocations = missingPostsResponse.body()!!.map { it.location[2] }
                                .distinct()
                        )
                    }
                } else {
                    _homeScreenUiState.update {
                        it.copy(
                            missingPosts = missingPostsResponse.body().orEmpty()
                        )
                    }
                }
            }
        }.invokeOnCompletion {
            _homeScreenUiState.update {
                it.copy(refreshing = false)
            }
        }
    }

    private fun getEncounteredPosts(
        locations: List<String> = listOf(),
        animals: List<String> = listOf()
    ) {
        viewModelScope.launch {
            _homeScreenUiState.update {
                it.copy(
                    refreshing = true
                )
            }
            val encounteredPostsResponse: Response<List<EncounteredPost>> = RetrofitObject.encounteredPostAPI.getLocalEncounteredPosts(
                countryCode = homeScreenUiState.value.device?.countryCode!!,
                locations = locations,
                animals = animals,
                auth = if(homeScreenUiState.value.showUserPostsOnly) homeScreenUiState.value.user!!.token else null
            )
            if(encounteredPostsResponse.isSuccessful && encounteredPostsResponse.body() != null) {
                if (locations.isEmpty() && animals.isEmpty()) {
                    _homeScreenUiState.update {
                        it.copy(
                            encounteredPosts = encounteredPostsResponse.body().orEmpty(),
                            availableAnimals = encounteredPostsResponse.body()!!.map { it.animalType }
                                .distinct(),
                            availableLocations = encounteredPostsResponse.body()!!.map { it.location[2] }
                                .distinct()
                        )
                    }
                } else {
                    _homeScreenUiState.update {
                        it.copy(
                            encounteredPosts = encounteredPostsResponse.body().orEmpty()
                        )
                    }
                }
            }
        }.invokeOnCompletion {
            _homeScreenUiState.update {
                it.copy(refreshing = false)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            val appUser = homeScreenUiState.value.user
            if (appUser != null) {
                RetrofitObject.userAPI.unsyncDevice(
                    homeScreenUiState.value.device?.deviceID!!,
                    appUser.token
                )
                appRepository.removeUser()
                Firebase.auth.signOut()
                googleSignInClient.signOut()
                _homeScreenUiState.update {
                    it.copy(
                        user = null,
                        availableAnimals = listOf(),
                        availableLocations = listOf(),
                    )
                }
                getPosts(defaultParams = true)
            }
        }
    }
}

enum class PostLocale {
    SameCountry, All
}