package com.example.artinspector.presentation.upload

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.artinspector.R
import com.example.artinspector.presentation.components.BitmapAndPlaceholderImage
import com.example.artinspector.presentation.components.MovingImageOverComposable
import com.example.artinspector.presentation.result.ResultScreenUiModel
import com.example.artinspector.viewmodels.upload.UploadFlowIntent
import com.example.artinspector.viewmodels.upload.UploadFlowSideEffects
import com.example.artinspector.viewmodels.upload.UploadViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun UploadMainScreen(
    getFileFromContentUri: suspend (Uri) -> File? = { null },
    onProcessImageResult: (ResultScreenUiModel) -> Unit = {  },
    uploadVm: UploadViewModel = viewModel(),
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        it?.let { imageUri ->
            uploadVm.handleIntent(UploadFlowIntent.UploadImageUriFromGallery(imageUri))
            scope.launch {
                val file = getFileFromContentUri(imageUri) ?: return@launch
                uploadVm.handleIntent(UploadFlowIntent.UploadImage(file))
            }
        }
    }
    // state
    val uploadState = uploadVm.uiState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        scope.launch {
            uploadVm.effects.collectLatest {
                when(it) {
                    is UploadFlowSideEffects.ShowErrorToast -> {
                        Toast.makeText(context, it.errorMsg, Toast.LENGTH_SHORT).show()
                    }
                    is UploadFlowSideEffects.NavigateToResultScreen -> {
                        Toast.makeText(context, "navigate", Toast.LENGTH_SHORT).show()
                        Log.d("NavigateTime", "with data: ${it.data} and bitmap: ${it.imageBitmap}")
                        val resultUiModel = ResultScreenUiModel(it.data, it.imageBitmap!!)
                        onProcessImageResult.invoke(resultUiModel)
                    }
                    is UploadFlowSideEffects.GetImageBitmapFromUri -> {
                        it.uri?.let {
                            val pickedImageBitmap = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                                MediaStore.Images
                                    .Media.getBitmap(context.contentResolver, it)
                            } else {
                                val source = ImageDecoder
                                    .createSource(context.contentResolver, it)
                                ImageDecoder.decodeBitmap(source)
                            }
                            uploadVm.handleIntent(UploadFlowIntent.UploadImageBitmap(pickedImageBitmap))
                        } ?: run { uploadVm.handleIntent(UploadFlowIntent.UploadImageBitmap(null)) }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        val shape = RoundedCornerShape(8.dp)

        MovingImageOverComposable(
            movingImageRes = R.drawable.ic_searching,
            shouldStartAnimating = uploadState.value.isLoading
        ) {
            BitmapAndPlaceholderImage(
                bitmap = uploadState.value.pickedImageBitmap,
                placeholderRes = R.drawable.ic_upload,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .aspectRatio(0.8f)
                    .clip(shape)
                    .border(BorderStroke(2.dp, Color.Black), shape)
                    .background(Color.LightGray, shape),
                placeholderContentScale = ContentScale.FillBounds
            )
        }

        Spacer(modifier = Modifier.height(50.dp))

        if (uploadState.value.shouldShowUploadImageButton) {
            Button(onClick = {
                imageLauncher.launch("image/*")
            }) {
                Text(text = "Click to upload image")
            }
        }
    }

}


@Preview(showSystemUi = true)
@Composable
fun UploadMainScreenPreview() {
    UploadMainScreen()
}