package com.example.animaltrackingui.viewModels

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.animaltrackingui.Utils
import com.example.animaltrackingui.db.AppRepository
import com.example.animaltrackingui.db.EncounteredPost
import com.example.animaltrackingui.db.MissingPost
import com.example.animaltrackingui.db.PostStatus
import com.example.animaltrackingui.retrofit.RetrofitObject
import com.example.animaltrackingui.services.ToastService
import com.example.animaltrackingui.states.CreatePostUiState
import com.example.animaltrackingui.states.PostType
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class CreatePostViewModel(private val appRepository: AppRepository, private val toastService: ToastService) : ViewModel() {
    private val _createPostUiState: MutableStateFlow<CreatePostUiState> =
        MutableStateFlow(CreatePostUiState())
    val createPostUiState: StateFlow<CreatePostUiState> =
        _createPostUiState.asStateFlow()
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        viewModelScope.launch {
            val user = appRepository.getUser() ?: return@launch
            _createPostUiState.update {
                it.copy(
                    user = user
                )
            }
        }.invokeOnCompletion {
            fetchAnimalTypes()
        }
    }

    fun onTextChange(text: String) {
        viewModelScope.launch {
            _createPostUiState.update {
                it.copy(
                    text = text
                )
            }
        }
    }
    fun onAnimalTypeChange(text: String) {
        viewModelScope.launch {
            _createPostUiState.update {
                it.copy(
                    animalType = text
                )
            }
        }
    }

    fun onAnimalNameChange(text: String) {
        viewModelScope.launch {
            _createPostUiState.update {
                it.copy(
                    animalName = text
                )
            }
        }
    }

    fun onContactInfoChange(text: String) {
        viewModelScope.launch {
            _createPostUiState.update {
                it.copy(
                    contactInfo = text
                )
            }
        }
    }

    fun onAnimalAgeChange(num: String) {
        var number = ""
        if(num.isDigitsOnly()){
            number = num
        }
        viewModelScope.launch {
            _createPostUiState.update {
                it.copy(
                    animalAge = number
                )
            }
        }
    }

    fun onAnimalImageUriChange(uri: Uri){
        viewModelScope.launch {
            _createPostUiState.update {
                it.copy(
                    animalImageUri = uri
                )
            }
        }
    }

    fun onLatLngChange(loc: LatLng){
        viewModelScope.launch {
            val location = Utils.getLocation(loc.latitude,loc.longitude)
            if(location == null){
                toastService.displayToast("The Location You Have Selected Is Invalid. Please Try Again.")
                return@launch
            }
            _createPostUiState.update {
                it.copy(
                    latitude = loc.latitude,
                    longitude = loc.longitude,
                    location = location
                )
            }
        }
    }

    fun onHostileChanged(hostile: Boolean){
        viewModelScope.launch {
            _createPostUiState.update {
                it.copy(
                    hostile = hostile
                )
            }
        }
    }

    fun onPostTypeChanged(postType: PostType){
        viewModelScope.launch {
            _createPostUiState.update {
                it.copy(
                    postType = postType
                )
            }
        }
    }

    fun onOpenAlertDialogChange(openAlertDialog: Boolean){
        viewModelScope.launch {
            if(createPostUiState.value.animalType == "Loading Animals..." || createPostUiState.value.text == "" || createPostUiState.value.latitude == 0.0 || createPostUiState.value.longitude == 0.0 || createPostUiState.value.animalImageUri.toString() == ""){
                toastService.displayToast("You Haven't Properly Filled Out All The Necessary Fields")
                return@launch
            }
            if(createPostUiState.value.postType == PostType.MISSING && (createPostUiState.value.animalName == "" || createPostUiState.value.animalAge == "" || createPostUiState.value.contactInfo == "")){
                toastService.displayToast("You Haven't Properly Filled Out All The Necessary Fields")
                return@launch
            }
            _createPostUiState.update {
                it.copy(
                    openAlertDialog = openAlertDialog
                )
            }
        }
    }
    fun onShowBottomSheetChange(showBottomSheet: Boolean){
        viewModelScope.launch {
            _createPostUiState.update {
                it.copy(
                    showBottomSheet = showBottomSheet
                )
            }
        }
    }
    fun onShowSelectedImageChange(showSelectedImage: Boolean){
        viewModelScope.launch {
            _createPostUiState.update {
                it.copy(
                    showSelectedImage = showSelectedImage
                )
            }
        }
    }
    fun onShowSelectedLocationChange(showSelectedLocation: Boolean){
        viewModelScope.launch {
            _createPostUiState.update {
                it.copy(
                    showSelectedLocation = showSelectedLocation
                )
            }
        }
    }

    fun onSelectedBottomSheetChange(selectedBottomSheet: Int){
        viewModelScope.launch {
            _createPostUiState.update {
                it.copy(
                    selectedBottomSheet = selectedBottomSheet
                )
            }
        }
    }

    fun onShowIdentificationDialogChange(showIdentificationDialog: Boolean){
        viewModelScope.launch {
            _createPostUiState.update {
                it.copy(
                    showIdentificationDialog = showIdentificationDialog
                )
            }
        }
    }

    private fun fetchAnimalTypes(){
        viewModelScope.launch {
            val animalsResponse = RetrofitObject.animalRecognitionAPI.getAnimalTypes("",
                createPostUiState.value.postType != PostType.ENCOUNTERED
            )
            if(animalsResponse.isSuccessful && animalsResponse.body() != null){
                Log.i("Animal Types", animalsResponse.body().toString())
                _createPostUiState.update {
                    it.copy(
                        animalTypeList = animalsResponse.body()!!,
                        animalType = animalsResponse.body()!!.first()
                    )
                }
            }
        }
    }

    fun uploadImageForIdentification(context: Context){
        scope.launch {
            Utils.uploadImageForIdentification(context, createPostUiState.value.animalImageUri, callback = {identifiedAnimal ->
                _createPostUiState.update {
                    it.copy(
                        showIdentificationDialog = identifiedAnimal != null,
                        identifiedAnimal = identifiedAnimal ?: createPostUiState.value.animalTypeList.first()
                    )
                }
            })
        }
    }

    fun uploadEncounteredPost(context: Context, navController: NavHostController) {
        viewModelScope.launch {
            val (url,ref) = Utils.uploadImageToFirebase(createPostUiState.value.animalImageUri, "encountered_posts", Firebase.auth.currentUser?.uid!!, context)
            val post = EncounteredPost(
                null,
                createPostUiState.value.user.username!!,
                createPostUiState.value.animalType,
                url,
                createPostUiState.value.text,
                createPostUiState.value.latitude,
                createPostUiState.value.longitude,
                createPostUiState.value.location,
                createPostUiState.value.location[0],
                null,
                false,
                false,
                PostStatus.NONE
            )
            val response = RetrofitObject.encounteredPostAPI.addEncounteredPost(
                post,
                createPostUiState.value.user.token
            )
            _createPostUiState.update {
                it.copy(openAlertDialog = false)
            }
            if (response.code() == 200) {
                navController.navigate("Main")
            } else {
                Toast.makeText(context, "There was an error while uploading your post. Try again later", Toast.LENGTH_SHORT).show()
                ref?.delete()?.await()
            }
        }
    }

    fun uploadMissingPost(context: Context, navController: NavHostController) {
        viewModelScope.launch {
            val (url,ref) = Utils.uploadImageToFirebase(createPostUiState.value.animalImageUri, "missing_posts", Firebase.auth.currentUser?.uid!!, context)
            val post = MissingPost(
                null,
                createPostUiState.value.user.username!!,
                createPostUiState.value.animalType,
                createPostUiState.value.animalName,
                createPostUiState.value.animalAge.toInt(),
                url,
                createPostUiState.value.text,
                createPostUiState.value.contactInfo,
                createPostUiState.value.latitude,
                createPostUiState.value.longitude,
                createPostUiState.value.location,
                createPostUiState.value.location[0],
                null,
                false,
                false,
                PostStatus.NONE
            )
            val response = RetrofitObject.missingPostAPI.addMissingPost(
                post,
                createPostUiState.value.user.token
            )
            _createPostUiState.update {
                it.copy(openAlertDialog = false)
            }
            if (response.code() == 200) {
                navController.navigate("Main")
            } else {
                Toast.makeText(context, "There was an error while uploading your post. Try again later", Toast.LENGTH_SHORT).show()
                ref?.delete()?.await()
            }
        }
    }
}