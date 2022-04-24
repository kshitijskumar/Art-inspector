package com.example.artinspector.domain.models

import com.google.gson.annotations.SerializedName

data class PredictionResponse(
    val similarResultsList: List<IndividualPredictionDetailsModel>? = listOf()
)

data class IndividualPredictionDetailsModel(
    val imageUrl: String? = null,
    val similarityPercentage: Double = 0.0,
    val artistName: String? = null,
    val genre: String? = null,
    val style: String? = null
)
