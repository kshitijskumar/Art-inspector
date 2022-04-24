package com.example.artinspector.viewmodels.upload

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artinspector.domain.models.PredictionResponse
import com.example.artinspector.domain.repositories.upload.UploadImageRepository
import com.example.artinspector.utils.DispatcherProviders
import com.example.artinspector.utils.Injector
import com.example.artinspector.utils.ResultState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class UploadViewModel(
    private val repo: UploadImageRepository = Injector.uploadRepository,
    private val dispatchers: DispatcherProviders = Injector.dispatchers
) : ViewModel() {

    private val _uiState = MutableStateFlow<UploadViewState>(UploadViewState())
    val uiState: StateFlow<UploadViewState> get() = _uiState

    private val _effects = MutableSharedFlow<UploadFlowSideEffects>()
    val effects = _effects.asSharedFlow()

    fun handleIntent(intent: UploadFlowIntent) {
        when(intent) {
            is UploadFlowIntent.UploadImage -> {
                uploadImageForPrediction(intent.file)
            }
            is UploadFlowIntent.UploadImageUriFromGallery -> {
                emitGetBitmapSideEffect(intent.uri)
            }
            is UploadFlowIntent.UploadImageBitmap -> {
                updateUploadImageBitmap(intent.bitmap)
            }
        }
    }

    private fun uploadImageForPrediction(file: File) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        withContext(dispatchers.io) {
            val result = repo.uploadImageForPrediction(file)
            delay(1000)
            when(result) {
                is ResultState.Success -> {
                    _uiState.update {
                        it.copy(
                            similarImagesResult = result.data,
                            isLoading = false
                        )
                    }
                    _effects.emit(
                        UploadFlowSideEffects.NavigateToResultScreen(
                            data = result.data,
                            imageBitmap = uiState.value.pickedImageBitmap
                        )
                    )
                }
                is ResultState.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            similarImagesResult = null,
                            pickedImageBitmap = null
                        )
                    }
                    _effects.emit(UploadFlowSideEffects.ShowErrorToast(result.errorMsg))
                }
            }
        }
    }

    private fun updateUploadImageBitmap(bitmap: Bitmap?) {
        _uiState.update { it.copy(pickedImageBitmap = bitmap) }
    }

    private fun emitGetBitmapSideEffect(uri: Uri?) = viewModelScope.launch {
        _effects.emit(UploadFlowSideEffects.GetImageBitmapFromUri(uri))
    }
}