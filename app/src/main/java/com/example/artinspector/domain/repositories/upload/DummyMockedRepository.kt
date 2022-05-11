package com.example.artinspector.domain.repositories.upload

import com.example.artinspector.domain.models.IndividualPredictionDetailsModel
import com.example.artinspector.domain.models.PredictionResponse
import com.example.artinspector.utils.ResultState
import kotlinx.coroutines.delay
import java.io.File

class DummyMockedRepository : UploadImageRepository {

    override suspend fun uploadImageForPrediction(imageFile: File): ResultState<PredictionResponse> {
        val dummyResult = listOf<IndividualPredictionDetailsModel>(
            IndividualPredictionDetailsModel(
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/art-inspector.appspot.com/o/subset_dataset%2FAlbrecht%20Durer%2F23.jpg?alt=media&token=62bca7d2-c86d-409a-92ab-d9ace8cfb2d9",
                similarityPercentage = 94.6,
                artistName = "Albrecht Durer",
                style = "out of this world!"
            ),
            IndividualPredictionDetailsModel(
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/art-inspector.appspot.com/o/subset_dataset%2FAlbrecht%20Durer%2F2293.jpg?alt=media&token=6cd9bebb-9a2d-4637-b30e-25ce73e6b28a",
                similarityPercentage = 92.1,
                artistName = "Albrecht Durer",
                style = "out of this world!"
            ),
            IndividualPredictionDetailsModel(
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/art-inspector.appspot.com/o/subset_dataset%2FAlbrecht%20Durer%2F3600.jpg?alt=media&token=8f637e75-f3af-499b-84ef-08f806fbe6e09",
                similarityPercentage = 86.8,
                artistName = "Albrecht Durer",
                style = "out of this world!"
            ),
            IndividualPredictionDetailsModel(
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/art-inspector.appspot.com/o/subset_dataset%2FAlbrecht%20Durer%2F1110.jpg?alt=media&token=ee764d0d-4175-4ec5-8320-01715798b439",
                similarityPercentage = 80.0,
                artistName = "Albrecht Durer",
                style = "out of this world!"
            ),
            IndividualPredictionDetailsModel(
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/art-inspector.appspot.com/o/subset_dataset%2FAlbrecht%20Durer%2F1053.jpg?alt=media&token=9d769cb3-2fb7-4538-8782-3976c9c8ce06",
                similarityPercentage = 79.6,
                artistName = "Albrecht Durer",
                style = "out of this world!"
            ),
            IndividualPredictionDetailsModel(
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/art-inspector.appspot.com/o/subset_dataset%2FAlbrecht%20Durer%2F1756.jpg?alt=media&token=bb58b785-8d55-455f-9444-7e456c09347e",
                similarityPercentage = 79.6,
                artistName = "Albrecht Durer",
                style = "out of this world!"
            ),

        )
        delay(2000)
        return ResultState.Success(
            PredictionResponse(
                similarResultsList = dummyResult.sortedByDescending { it.similarityPercentage }
            )
        )
    }
}