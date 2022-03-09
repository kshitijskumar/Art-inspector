package com.example.artinspector.domain.repositories.upload

import android.util.Log
import com.example.artinspector.domain.api.UploadApiService
import com.example.artinspector.utils.DispatcherProviders
import com.example.artinspector.utils.Injector
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.Exception

class DefaultUploadImageRepository(
    private val api: UploadApiService = Injector.uploadApiService,
    private val dispatchers: DispatcherProviders = Injector.dispatchers
) : UploadImageRepository {

    override suspend fun uploadImageForPrediction(imageFile: File) {
        withContext(dispatchers.io) {
            try {
                val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile)
                val imagePart = MultipartBody.Part.createFormData("file", imageFile.name, requestFile)

                val result = api.uploadImage(image = imagePart)
                Log.d("UploadError", "resp: $result")
            } catch (e: Exception) {
                Log.d("UploadError", "exception: $e")
            }
        }
    }
}