package com.example.artinspector.domain.repositories.upload

import android.net.Uri
import java.io.File

interface UploadImageRepository {

    suspend fun uploadImageForPrediction(imageFile: File)
}