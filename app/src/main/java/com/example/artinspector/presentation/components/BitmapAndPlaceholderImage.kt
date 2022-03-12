package com.example.artinspector.presentation.components

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun BitmapAndPlaceholderImage(
    bitmap: Bitmap?,
    @DrawableRes placeholderRes: Int,
    modifier: Modifier = Modifier,
    bitmapContentScale: ContentScale = ContentScale.None,
    placeholderContentScale: ContentScale = ContentScale.None
) {
    bitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = "picked image",
            modifier = modifier,
            contentScale = bitmapContentScale
        )
    } ?: run {
        Image(
            painter = painterResource(id = placeholderRes),
            contentDescription = "nothing",
            modifier = modifier,
            contentScale = placeholderContentScale
        )
    }
}