package com.example.artinspector.presentation.components

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artinspector.R
import com.example.artinspector.domain.models.IndividualPredictionDetailsModel
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun TopSimilarImageComposable(
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
    firstImage: Bitmap? = null,
    similarImageDetails: IndividualPredictionDetailsModel?
) {

    Column(
        modifier = modifier.wrapContentHeight(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            BitmapAndPlaceholderImage(
                bitmap = firstImage,
                placeholderRes = R.drawable.ic_upload,
                modifier = imageModifier,
                bitmapContentScale = ContentScale.Fit
            )
            Spacer(
                modifier = Modifier
                    .width(12.dp)
                    .background(Black)
            )
            GlideImage(
                imageModel = similarImageDetails?.imageUrl,
                modifier = imageModifier,
                placeHolder = painterResource(id = R.drawable.ic_upload),
                contentScale = ContentScale.Inside,
                error = painterResource(id = R.drawable.ic_upload)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp)
                .wrapContentHeight()
                .clip(RoundedCornerShape(10.dp))
                .border(width = 2.dp, color = Black, shape = RoundedCornerShape(10.dp))
                .background(Color.White)
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Most similar artwork by,",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = similarImageDetails?.artistName.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${similarImageDetails?.similarityPercentage} % similarity",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

@Preview
@Composable
fun TopSimilarImageComposablePreview() {

}