package com.example.animaltrackingui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.animaltrackingui.db.AppRepository
import com.example.animaltrackingui.db.PostStatus
import com.example.animaltrackingui.db.Report
import com.example.animaltrackingui.retrofit.RetrofitObject
import com.example.animaltrackingui.states.MissingPostUiState
import com.example.animaltrackingui.states.PostType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MissingPostViewModel(private val appRepository: AppRepository): ViewModel() {
    private val _missingPostUiState: MutableStateFlow<MissingPostUiState> =
        MutableStateFlow(MissingPostUiState())
    val missingPostUiState: StateFlow<MissingPostUiState> = _missingPostUiState.asStateFlow()

    init {
        viewModelScope.launch {
            _missingPostUiState.update {
                it.copy(
                    user = appRepository.getUser()
                )
            }
        }
    }


    fun getPost(id: Long){
        viewModelScope.launch {
            val postResponse = RetrofitObject.missingPostAPI.getMissingPost(id)
            if(postResponse.body() != null) {
                _missingPostUiState.update {
                    it.copy(
                        missingPost = postResponse.body()!!,
                        report = Report(null, postResponse.body()!!.id!!,"",missingPostUiState.value.user?.username,"",
                            PostType.MISSING)
                    )
                }
            }
        }
    }

    fun onReportCommentChanged(text: String){
        viewModelScope.launch {
            val report = missingPostUiState.value.report
            report.comment = text
            _missingPostUiState.update {
                it.copy(
                    report = report
                )
            }
        }
    }

    fun onReportReasonChanged(text:String){
        viewModelScope.launch {
            val report = missingPostUiState.value.report
            report.reason = text
            _missingPostUiState.update {
                it.copy(
                    report = report
                )
            }
        }
    }

    fun onPostStatusChanged(postStatus: PostStatus){
        viewModelScope.launch {
            _missingPostUiState.update {
                it.copy(
                    postStatus = postStatus
                )
            }
        }
    }

    fun deletePost(navController: NavHostController){
        viewModelScope.launch {
            if (missingPostUiState.value.user?.username == missingPostUiState.value.missingPost.username){
                val deleteResponse = RetrofitObject.missingPostAPI.deleteMissingPost(missingPostUiState.value.missingPost.id!!,
                    missingPostUiState.value.user!!.token)
                if(deleteResponse.isSuccessful && deleteResponse.body() != null){
                    navController.navigate("Main")
                }
            }
        }
    }
    fun reportPost(){
        viewModelScope.launch {
            val reportResponse = RetrofitObject.reportAPI.sendReport(missingPostUiState.value.report)
        }
    }

    fun updatePostStatus(){
        viewModelScope.launch {
            if(missingPostUiState.value.user != null) {
                val updateStatusResponse = RetrofitObject.missingPostAPI.updateStatus(
                    missingPostUiState.value.missingPost.id!!,
                    missingPostUiState.value.postStatus,
                    missingPostUiState.value.user?.token!!
                )
            }
        }
    }
}