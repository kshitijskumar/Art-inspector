package com.example.artinspector.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MovingImageOverComposable(
    @DrawableRes movingImageRes: Int,
    shouldStartAnimating: Boolean = false,
    subjectComposable: @Composable () -> Unit,
) {

    var imageBounds = BoundsAndRandomPoints(0f, 0f, 0f, 0f)

    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }

    val targetValueX = remember { mutableStateOf(0f) }
    val targetValueY = remember { mutableStateOf(0f) }

    val listOfJobs = mutableListOf<Job>()


    LaunchedEffect(key1 = shouldStartAnimating) {
        if (shouldStartAnimating) {
            val job = scope.launch {
                repeat(10) {
                    val randomTargetPoints = imageBounds.getRandomPointBetweenBounds()
                    targetValueX.value = randomTargetPoints.first
                    targetValueY.value = randomTargetPoints.second
                    delay(500)
                }
            }
            listOfJobs.add(job)
        } else  {
            listOfJobs.forEach { it.cancel() }
            listOfJobs.clear()
        }
    }

    Box(
        modifier = Modifier
            .onGloballyPositioned {
                imageBounds = BoundsAndRandomPoints(
                    topX = 0f,
                    topY = 0f,
                    bottomX = it.size.width.toFloat(),
                    bottomY = it.size.height.toFloat()
                )
            }
    ) {
        subjectComposable.invoke()
        if (shouldStartAnimating) {
            Image(
                painter = painterResource(id = movingImageRes),
                contentDescription = "logo",
                modifier = Modifier
                    .size(50.dp)
                    .offset {
                        IntOffset(
                            offsetX.value.toInt(),
                            offsetY.value.toInt()
                        )
                    }
            )
            LaunchedEffect(key1 = shouldStartAnimating, key2 = targetValueX.value, key3 = targetValueY.value) {
                if (shouldStartAnimating) {
                    scope.launch {
                        launch {
                            offsetX.animateTo(
                                targetValue = targetValueX.value,
                                animationSpec = tween(durationMillis = 500),
                            )
                        }

                        launch {
                            offsetY.animateTo(
                                targetValue = targetValueY.value,
                                animationSpec = tween(durationMillis = 500)
                            )
                        }
                    }
                }
            }
        }
    }
}


data class BoundsAndRandomPoints(
    val topX: Float,
    val topY: Float,
    val bottomX: Float,
    val bottomY: Float
) {
    companion object {
        private const val OFFSET_LIMIT = 100
    }
    fun getRandomPointBetweenBounds(): Pair<Float, Float> {
        val randomX = getRandomBetweenTwo(topX, bottomX)
        val randomY = getRandomBetweenTwo(topY, bottomY)

        return Pair(randomX, randomY)
    }

    private fun getRandomBetweenTwo(p1: Float, p2: Float): Float {
        return (p1.toInt() + OFFSET_LIMIT..p2.toInt() - OFFSET_LIMIT).random().toFloat()
    }
}