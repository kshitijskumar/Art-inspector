package com.example.artinspector.utils

import com.example.artinspector.domain.api.UploadApiService
import com.example.artinspector.domain.repositories.upload.DefaultUploadImageRepository
import com.example.artinspector.domain.repositories.upload.UploadImageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Injector {

    private fun provideRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val uploadApiService: UploadApiService
        get() {
            return provideRetrofit("https://image-classifier-flask-demo.herokuapp.com/")
                .create(UploadApiService::class.java)
        }

    val dispatchers: DispatcherProviders by lazy {
        object : DispatcherProviders {
            override val io: CoroutineDispatcher
                get() = Dispatchers.IO
            override val main: CoroutineDispatcher
                get() = Dispatchers.Main
        }
    }

    val uploadRepository: UploadImageRepository
        get() = DefaultUploadImageRepository()

}