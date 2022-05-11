package com.example.artinspector.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artinspector.domain.models.IndividualPredictionDetailsModel
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun GenericSimilarImage(
    modifier: Modifier = Modifier,
    item: IndividualPredictionDetailsModel
) {

    Row(
        modifier = modifier
    ) {
        GlideImage(
            imageModel = item.imageUrl,
            modifier = Modifier
                .width(80.dp)
                .aspectRatio(0.8f)
        )
        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.artistName.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${item.similarityPercentage} % similarity",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }

}