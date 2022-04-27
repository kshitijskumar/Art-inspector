package com.example.artinspector.viewmodels.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artinspector.presentation.result.ResultScreenUiModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResultScreenViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ResultScreenState())
    val uiState = _uiState.asStateFlow()

    private val _resultSideEffects = Channel<ResultScreenSideEffects>(Channel.BUFFERED)
    val resultSideEffects = _resultSideEffects.receiveAsFlow()

    fun handleIntent(intent: ResultScreenIntent) {
        when(intent) {
            is ResultScreenIntent.UpdateStateWithActualData -> {
                handleUpdatingStateWithActualData(intent.resultModel)
            }
        }
    }

    private fun handleUpdatingStateWithActualData(resultUiModel: ResultScreenUiModel) = viewModelScope.launch {
        if (resultUiModel.predictionResponse.similarResultsList.isNullOrEmpty()) {
            _resultSideEffects.trySend(ResultScreenSideEffects.NoResultsFoundErrorToast("No relevant similar images found"))
        }
        _uiState.update {
            it.copy(
                uploadedImageBitmap = resultUiModel.uploadedImageBitmap,
                topPredictedResult = resultUiModel.predictionResponse.similarResultsList?.getOrNull(0),
                remainingPredictedResult = resultUiModel.predictionResponse.similarResultsList?.drop(1) ?: listOf()
            )
        }
    }
}