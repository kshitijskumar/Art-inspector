package com.example.artinspector.presentation.result

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.artinspector.R
import com.example.artinspector.presentation.components.BitmapAndPlaceholderImage
import com.example.artinspector.presentation.components.GenericSimilarImage
import com.example.artinspector.presentation.components.TopSimilarImageComposable
import com.example.artinspector.ui.theme.LightestGrey
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
    val totalWidthForImages = getScreenWidth() - 24.dp.value

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                TopSimilarImageComposable(
                    similarImageDetails = state.topPredictedResult,
                    firstImage = state.uploadedImageBitmap,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .wrapContentHeight(),
                    imageModifier = Modifier
                        .width((totalWidthForImages / 2).dp)
                        .height(250.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            items(
                items = state.remainingPredictedResult
            ) { item ->
                Spacer(modifier = Modifier.height(14.dp))
                GenericSimilarImage(
                    item = item,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .background(LightestGrey, shape = RoundedCornerShape(10.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }
    }
}

@Composable
fun getScreenWidth(): Int {
    val configuration = LocalConfiguration.current
    return configuration.screenWidthDp
}