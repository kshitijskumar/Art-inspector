package com.example.artinspector.domain.repositories.upload

import com.example.artinspector.domain.models.IndividualPredictionDetailsModel
import com.example.artinspector.domain.models.PredictionResponse
import com.example.artinspector.utils.ResultState
import kotlinx.coroutines.delay
import java.io.File

class DummyMockedRepository : UploadImageRepository {

    override suspend fun uploadImageForPrediction(imageFile: File): ResultState<PredictionResponse> {
        val dummyResult = (0..10).map {
            IndividualPredictionDetailsModel(
                imageUrl = "",
                similarityPercentage = 100.0,
                artistName = "Majnu bhai",
                style = "out of this world!"
            )
        }
        delay(2000)
        return ResultState.Success(
            PredictionResponse(
                similarResultsList = dummyResult
            )
        )
    }
}