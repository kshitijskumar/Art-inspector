package com.example.artinspector.domain.repositories.upload

import com.example.artinspector.domain.models.PredictionResponse
import com.example.artinspector.utils.ResultState
import java.io.File

interface UploadImageRepository {

    suspend fun uploadImageForPrediction(imageFile: File) : ResultState<PredictionResponse>

}