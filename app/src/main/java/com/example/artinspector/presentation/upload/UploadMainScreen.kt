package com.example.artinspector.presentation.upload

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
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
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.artinspector.domain.models.PredictionResponse
import com.example.artinspector.presentation.components.BitmapAndPlaceholderImage
import com.example.artinspector.presentation.components.MovingImageOverComposable
import com.example.artinspector.utils.ResultState
import com.example.artinspector.utils.performIfInstanceOf
import com.example.artinspector.viewmodels.upload.UploadViewModel
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun UploadMainScreen(
    getFileFromContentUri: suspend (Uri) -> File? = { null },
    uploadVm: UploadViewModel = viewModel(),
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var pickedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var pickedImageBitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        it?.let { imageUri ->
            pickedImageUri = imageUri
            uploadVm.updateUiForStateRequest(UploadViewModel.UiStateRequest.Loading)
            scope.launch {
                val file = getFileFromContentUri(imageUri) ?: return@launch
                uploadVm.uploadImageForPrediction(file = file)
            }
        }
    }
    // state
    val uploadState = uploadVm.uploadImageState.observeAsState()

    // error toast
    uploadState.value.performIfInstanceOf<ResultState.Error> {
        val errorMsg = this.errorMsg
        LaunchedEffect(key1 = this.errorMsg) {
            pickedImageUri = null
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
        }
    }

    uploadState.value.performIfInstanceOf<ResultState.Success<PredictionResponse>> {
        LaunchedEffect(key1 = this.data) {
            Toast.makeText(context, this@performIfInstanceOf.data.predictionResult.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    pickedImageUri?.let {
        pickedImageBitmap = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            MediaStore.Images
                .Media.getBitmap(context.contentResolver, it)
        } else {
            val source = ImageDecoder
                .createSource(context.contentResolver, it)
            ImageDecoder.decodeBitmap(source)
        }
    } ?: run { pickedImageBitmap = null }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        val shape = RoundedCornerShape(8.dp)

        MovingImageOverComposable(
            movingImageRes = R.drawable.ic_searching,
            shouldStartAnimating = uploadState.value is ResultState.Loading
        ) {
            BitmapAndPlaceholderImage(
                bitmap = pickedImageBitmap,
                placeholderRes = R.drawable.ic_upload,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .aspectRatio(0.8f)
                    .clip(shape)
                    .border(BorderStroke(2.dp, Color.Black), shape)
                    .background(Color.LightGray, shape),
                placeholderContentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(50.dp))

        if (uploadState.value != ResultState.Loading) {
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