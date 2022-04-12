package com.example.artinspector.presentation.navigation

import com.example.artinspector.domain.models.PredictionResponse

object NavigationHelper {

    val uploadScreenRoute: String = "uploadScreen"
    val resultScreenRoute: String = "resultScreen/{response}/{filePath}"

    fun getArgumentsListForResultScreen(response: PredictionResponse, filePath: String) {

    }
}