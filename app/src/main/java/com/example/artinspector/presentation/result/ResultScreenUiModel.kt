package com.example.artinspector.presentation.result

import android.graphics.Bitmap
import com.example.artinspector.domain.models.PredictionResponse

data class ResultScreenUiModel(
    val predictionResponse: PredictionResponse,
    val uploadedImageBitmap: Bitmap
)