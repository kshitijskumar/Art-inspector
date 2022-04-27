package com.example.artinspector.presentation.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.artinspector.domain.models.PredictionResponse
import com.example.artinspector.presentation.result.ResultScreenUiModel
import com.google.gson.Gson

object NavigationHelper {

    val uploadScreenRoute: String = "uploadScreen"
    val resultScreenRoute: String = "resultScreen/{result-data}"

    fun getNamedNAvArgumentsListForResultScreen(): List<NamedNavArgument> {
        return listOf(navArgument("result-data"){ type = NavType.StringType })
    }


    fun getUpdatedPathToNavigateToResultScreen(resultModel: ResultScreenUiModel): String {
        val resultModelInStringJson = Gson().toJson(resultModel) ?: ""
        return "resultScreen/$resultModelInStringJson"
    }
}