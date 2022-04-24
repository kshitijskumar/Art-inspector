package com.example.artinspector.viewmodels.upload

import android.graphics.Bitmap
import android.net.Uri
import com.example.artinspector.domain.models.PredictionResponse
import java.io.File

sealed class UploadFlowIntent {
    data class UploadImage(val file: File) : UploadFlowIntent()
    data class UploadImageUriFromGallery(val uri: Uri?) : UploadFlowIntent()
    data class UploadImageBitmap(val bitmap: Bitmap?) : UploadFlowIntent()
}

data class UploadViewState(
    val similarImagesResult: PredictionResponse? = null,
    val isLoading: Boolean = false,
    val pickedImageBitmap: Bitmap? = null
) {
    val shouldShowUploadImageButton: Boolean get() = !isLoading
}

sealed class UploadFlowSideEffects {
    data class ShowErrorToast(val errorMsg: String) : UploadFlowSideEffects()
    data class NavigateToResultScreen(val data: PredictionResponse, val imageBitmap: Bitmap?) : UploadFlowSideEffects()
    data class GetImageBitmapFromUri(val uri: Uri?) : UploadFlowSideEffects()
}