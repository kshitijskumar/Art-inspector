package com.example.artinspector.viewmodels.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artinspector.domain.models.PredictionResponse
import com.example.artinspector.domain.repositories.upload.UploadImageRepository
import com.example.artinspector.utils.DispatcherProviders
import com.example.artinspector.utils.Injector
import com.example.artinspector.utils.ResultState
import com.example.artinspector.utils.performIfInstanceOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class UploadViewModel(
    private val repo: UploadImageRepository = Injector.uploadRepository,
    private val dispatchers: DispatcherProviders = Injector.dispatchers
) : ViewModel() {

    private val _uploadImageState = MutableLiveData<ResultState<PredictionResponse>>()
    val uploadImageState: LiveData<ResultState<PredictionResponse>> get() = _uploadImageState

    fun uploadImageForPrediction(file: File) = viewModelScope.launch {
        _uploadImageState.postValue(ResultState.Loading)
        withContext(dispatchers.io) {
            val result = repo.uploadImageForPrediction(file)
            _uploadImageState.postValue(result)
        }
    }

    fun updateUiForStateRequest(req: UiStateRequest) {
        if (req == UiStateRequest.Loading) {
            _uploadImageState.postValue(ResultState.Loading)
        }
    }

    enum class UiStateRequest {
        Loading, Success, Error
    }
}