package com.example.artinspector.presentation.upload

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.artinspector.utils.Injector
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun UploadMainScreen(
    getFileFromContentUri: suspend (Uri) -> File? = { null }
) {

    val scope = rememberCoroutineScope()

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        it?.let { imageUri ->
            scope.launch {
                val file = getFileFromContentUri(imageUri) ?: return@launch
                Injector.uploadRepository.uploadImageForPrediction(imageFile = file)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Button(onClick = {
            imageLauncher.launch("image/*")
        }) {
            Text(text = "Click to upload")
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun UploadMainScreenPreview() {
    UploadMainScreen()
}