package com.example.artinspector.domain.models

import com.google.gson.annotations.SerializedName

data class PredictionResponse(
    @SerializedName("predicted class name")
    val predictionResult: String? = null
)
