package com.example.artinspector.domain.repositories.upload

import android.util.Log
import com.example.artinspector.domain.api.UploadApiService
import com.example.artinspector.domain.models.PredictionResponse
import com.example.artinspector.utils.DispatcherProviders
import com.example.artinspector.utils.Injector
import com.example.artinspector.utils.ResultState
import com.example.artinspector.utils.safeApiCall
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class DefaultUploadImageRepository(
    private val api: UploadApiService = Injector.uploadApiService
) : UploadImageRepository {

    override suspend fun uploadImageForPrediction(imageFile: File): ResultState<PredictionResponse> {
        return safeApiCall {
            val requestFile = imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("file", imageFile.name, requestFile)

            api.uploadImage(image = imagePart)
        }
    }
}