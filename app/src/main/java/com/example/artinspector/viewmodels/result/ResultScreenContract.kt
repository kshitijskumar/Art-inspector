package com.example.artinspector.viewmodels.result

import android.graphics.Bitmap
import com.example.artinspector.domain.models.IndividualPredictionDetailsModel
import com.example.artinspector.presentation.result.ResultScreenUiModel

data class ResultScreenState(
    val uploadedImageBitmap: Bitmap? = null,
    val topPredictedResult: IndividualPredictionDetailsModel? = null,
    val remainingPredictedResult: List<IndividualPredictionDetailsModel> = listOf()
)

sealed class ResultScreenIntent {
    data class UpdateStateWithActualData(val resultModel: ResultScreenUiModel) : ResultScreenIntent()
}

sealed class ResultScreenSideEffects {
    data class NoResultsFoundErrorToast(val errorMsg: String) : ResultScreenSideEffects()
}