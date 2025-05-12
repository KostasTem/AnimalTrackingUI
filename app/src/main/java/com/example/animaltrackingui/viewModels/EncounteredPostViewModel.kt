package com.example.animaltrackingui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.animaltrackingui.db.AppRepository
import com.example.animaltrackingui.db.PostStatus
import com.example.animaltrackingui.db.Report
import com.example.animaltrackingui.retrofit.RetrofitObject
import com.example.animaltrackingui.states.EncounteredPostUiState
import com.example.animaltrackingui.states.PostType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EncounteredPostViewModel(private val appRepository: AppRepository): ViewModel() {
    private val _encounteredPostUiState: MutableStateFlow<EncounteredPostUiState> =
        MutableStateFlow(EncounteredPostUiState())
    val encounteredPostUiState: StateFlow<EncounteredPostUiState> = _encounteredPostUiState.asStateFlow()

    init {
        viewModelScope.launch {
            _encounteredPostUiState.update {
                it.copy(
                    user = appRepository.getUser(),
                )
            }
        }
    }

    fun getPost(id: Long){
        viewModelScope.launch {
            val postResponse = RetrofitObject.encounteredPostAPI.getEncounteredPost(id)
            if(postResponse.body() != null) {
                _encounteredPostUiState.update {
                    it.copy(
                        encounteredPost = postResponse.body()!!,
                        report = Report(null, postResponse.body()!!.id!!,"",encounteredPostUiState.value.user?.username,"",PostType.ENCOUNTERED)
                    )
                }
            }
        }
    }

    fun onReportCommentChanged(text: String){
        viewModelScope.launch {
            val report = encounteredPostUiState.value.report
            report.comment = text
            _encounteredPostUiState.update {
                it.copy(
                    report = report
                )
            }
        }
    }

    fun onReportReasonChanged(text:String){
        viewModelScope.launch {
            val report = encounteredPostUiState.value.report
            report.reason = text
            _encounteredPostUiState.update {
                it.copy(
                    report = report
                )
            }
        }
    }

    fun onPostStatusChanged(postStatus: PostStatus){
        viewModelScope.launch {
            _encounteredPostUiState.update {
                it.copy(
                    postStatus = postStatus
                )
            }
        }
    }

    fun deletePost(navController:NavHostController){
        viewModelScope.launch {
            if (encounteredPostUiState.value.user?.username == encounteredPostUiState.value.encounteredPost.username){
                val deleteResponse = RetrofitObject.encounteredPostAPI.deleteEncounteredPost(encounteredPostUiState.value.encounteredPost.id!!,encounteredPostUiState.value.user!!.token)
                if(deleteResponse.isSuccessful && deleteResponse.body() != null){
                    navController.navigate("Main")
                }
            }
        }
    }

    fun reportPost(){
        viewModelScope.launch {
            val reportResponse = RetrofitObject.reportAPI.sendReport(encounteredPostUiState.value.report)
        }
    }

    fun updatePostStatus(){
        viewModelScope.launch {
            if(encounteredPostUiState.value.user != null) {
                val updateStatusResponse = RetrofitObject.encounteredPostAPI.updateStatus(
                    encounteredPostUiState.value.encounteredPost.id!!,
                    encounteredPostUiState.value.postStatus,
                    encounteredPostUiState.value.user?.token!!
                )
            }
        }
    }


}