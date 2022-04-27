package com.example.artinspector.presentation.result

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.artinspector.R
import com.example.artinspector.presentation.components.BitmapAndPlaceholderImage
import com.example.artinspector.viewmodels.result.ResultScreenIntent
import com.example.artinspector.viewmodels.result.ResultScreenSideEffects
import com.example.artinspector.viewmodels.result.ResultScreenViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Composable
fun ResultMainScreen(
    resultScreenUiModel: ResultScreenUiModel,
    resultVm: ResultScreenViewModel = viewModel()
) {

    LaunchedEffect(key1 = resultVm) {
        resultVm.handleIntent(ResultScreenIntent.UpdateStateWithActualData(resultScreenUiModel))
    }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val state by resultVm.uiState.collectAsState()

    LaunchedEffect(key1 = resultVm) {
        scope.launch {
            resultVm.resultSideEffects
                .onEach {
                    when(it) {
                        is ResultScreenSideEffects.NoResultsFoundErrorToast -> {
                            Toast.makeText(context, it.errorMsg, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .collect()
        }
    }

    BitmapAndPlaceholderImage(
        bitmap = state.uploadedImageBitmap,
        placeholderRes = R.drawable.ic_upload,
        modifier = Modifier
            .fillMaxWidth(fraction = 0.6f)
            .aspectRatio(0.8f)
    )
}