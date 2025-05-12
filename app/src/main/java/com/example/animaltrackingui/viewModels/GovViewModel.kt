package com.example.animaltrackingui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animaltrackingui.db.AppRepository
import com.example.animaltrackingui.retrofit.RetrofitObject
import com.example.animaltrackingui.states.GovUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GovViewModel(private val appRepository: AppRepository) : ViewModel() {

    private val _govUiState: MutableStateFlow<GovUiState> =
        MutableStateFlow(GovUiState())
    val govUiState: StateFlow<GovUiState> =
        _govUiState.asStateFlow()


    init {
        viewModelScope.launch {
            _govUiState.update {
                it.copy(
                    user = appRepository.getUser(),
                    device = appRepository.getDevice()
                )
            }
        }.invokeOnCompletion {
            if(govUiState.value.user!=null) {
                getPosts()
            }
        }
    }


    fun getPosts() {
        when (govUiState.value.periodIndex) {
            0 -> {
                getMissingPosts(govUiState.value.user?.countryCode!!)
            }

            1 -> {
                getEncounteredPosts(govUiState.value.user?.countryCode!!)
            }
        }
    }

    fun selectedFilterCallback(animals: List<String>, locations: List<String>){
        viewModelScope.launch {
            when(govUiState.value.periodIndex){
                0 -> {
                    getMissingPosts(govUiState.value.user?.countryCode!!, locations, animals)
                }
                1 -> {
                    getEncounteredPosts(govUiState.value.user?.countryCode!!,locations,animals)
                }
            }
        }
    }

    private fun getMissingPosts(countryCode: String, locations: List<String> = listOf(), animals: List<String> = listOf()) {
        viewModelScope.launch {
            _govUiState.update {
            it.copy(
                refreshing = true
            )
        }
            //val missingPostsResponse = RetrofitObject.missingPostAPI.getLocalMissingPosts(govUiState.value.device?.countryCode!!)
            val missingPostsResponse = RetrofitObject.missingPostAPI.getMissingPostsForGov(countryCode = countryCode, locations = locations, animals = animals, auth = govUiState.value.user?.token!!)
            val mainUpdated = if(locations.isEmpty() && animals.isEmpty()) {govUiState.value.mainUpdated + 1} else {govUiState.value.mainUpdated}
            _govUiState.update {
                it.copy(
                    missingPosts = missingPostsResponse.body().orEmpty(),
                    mainUpdated = mainUpdated,
                    refreshing = false
                )
            }
        }
    }

    private fun getEncounteredPosts(countryCode: String, locations: List<String> = listOf(), animals: List<String> = listOf()) {
        viewModelScope.launch {
            _govUiState.update {
            it.copy(
                refreshing = true
            )
        }
            //val encounteredPostsResponse = RetrofitObject.encounteredPostAPI.getLocalEncounteredPosts(govUiState.value.device?.countryCode!!)
            val encounteredPostsResponse = RetrofitObject.encounteredPostAPI.getEncounteredPostsForGov(countryCode = countryCode, locations = locations, animals = animals, auth = govUiState.value.user?.token!!)
            val mainUpdated = if(locations.isEmpty() && animals.isEmpty()) {govUiState.value.mainUpdated + 1} else {govUiState.value.mainUpdated}
            _govUiState.update {
                it.copy(
                    encounteredPosts = encounteredPostsResponse.body().orEmpty(),
                    mainUpdated = mainUpdated,
                    refreshing = false
                )
            }
        }
    }
    fun sortingCallback(sortingValue: String) {
        when (govUiState.value.periodIndex) {
            0 -> {
                when (sortingValue) {
                    "Post Date" -> {
                        _govUiState.update {
                            it.copy(
                                missingPosts = govUiState.value.missingPosts.sortedByDescending { post -> post.timestamp }
                            )
                        }
                    }

                    "Post Animal" -> {
                        _govUiState.update {
                            it.copy(
                                missingPosts = govUiState.value.missingPosts
                                    .groupBy { post -> post.animalType }
                                    .toSortedMap(compareBy { key -> key }).values
                                    .flatten()
                            )
                        }
                    }

                    "Post Location" -> {
                        _govUiState.update {
                            it.copy(
                                missingPosts = govUiState.value.missingPosts
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
                        _govUiState.update {
                            it.copy(
                                encounteredPosts = govUiState.value.encounteredPosts.sortedByDescending { post -> post.timestamp }
                            )
                        }
                    }

                    "Post Animal" -> {
                        _govUiState.update {
                            it.copy(
                                encounteredPosts = govUiState.value.encounteredPosts
                                    .groupBy { post -> post.animalType }
                                    .toSortedMap(compareBy { key -> key }).values
                                    .flatten()
                            )
                        }
                    }

                    "Post Location" -> {
                        _govUiState.update {
                            it.copy(
                                encounteredPosts = govUiState.value.encounteredPosts
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


    fun updatePeriodIndex(index: Int) {
        if (govUiState.value.periodIndex != index) {
            when (index) {
                0 -> {
                    getMissingPosts(govUiState.value.user?.countryCode!!)
                }

                1 -> {
                    getEncounteredPosts(govUiState.value.user?.countryCode!!)
                }
            }
            viewModelScope.launch {
                _govUiState.update {
                    it.copy(
                        periodIndex = index
                    )
                }
            }
        }
    }
}