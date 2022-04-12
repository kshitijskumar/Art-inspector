package com.example.artinspector

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.artinspector.domain.models.PredictionResponse
import com.example.artinspector.presentation.navigation.NavigationHelper.uploadScreenRoute
import com.example.artinspector.presentation.upload.UploadMainScreen
import com.example.artinspector.ui.theme.ArtInspectorTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.lang.Exception

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtInspectorTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = uploadScreenRoute) {
                        composable(route = uploadScreenRoute) {
                            UploadMainScreen(
                                getFileFromContentUri = this@MainActivity::getFileFromContentUri,
                                onProcessImageResult = { response: PredictionResponse, imageFile: File? ->

                                }
                            )
                        }

                    }
                }
            }
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun getFileFromContentUri(contentUri: Uri): File? {
        return withContext(Dispatchers.IO) {
            var inputStream: InputStream? = null
            var outputStream: FileOutputStream? = null
            var finalFile: File? = null

            try {
                inputStream = contentResolver?.openInputStream(contentUri)
                val inputBytes = inputStream?.readBytes() ?: byteArrayOf()
                finalFile = File(externalCacheDir, "InspecImg${System.currentTimeMillis()}")

                if (finalFile.exists()) {
                    finalFile.delete()
                }

                outputStream = FileOutputStream(finalFile.path)
                outputStream.write(inputBytes)

            } catch (e: Exception) {
                Log.d("UploadError", "on creating file: $e")
            } finally {
                inputStream?.close()
                outputStream?.close()
            }

            finalFile
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ArtInspectorTheme {
        Greeting("Android")
    }
}