package com.example.artinspector.domain.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadApiService {

    @Multipart
    @POST("predict")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ) : Response<Any>
}