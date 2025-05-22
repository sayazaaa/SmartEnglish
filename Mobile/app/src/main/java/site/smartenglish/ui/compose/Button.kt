package site.smartenglish.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImagePainter
import site.smartenglish.ui.theme.LightOrange
import site.smartenglish.ui.theme.White

@Composable
fun WideButton(
    text: String,
    onClick: () -> Unit,
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .width(342.dp)
            .height(53.dp),
        content = {
            Text(
                text,
                fontSize = 19.sp,
            )
        }
    )
}

@Composable
fun GlassButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    number: String,
    backgroundImagePainter: AsyncImagePainter,
) {

    Box(
        modifier = modifier
            .clickable(onClick = onClick)
            .fillMaxSize()
    ) {
        Image(
            painter = backgroundImagePainter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationX = 0f
                    translationY = 10f
                }
                .clip(RoundedCornerShape(8.dp))
        )
        // 内容
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = text,
                fontSize = 22.sp,
                color = White
            )
            Text(
                text = number,
                fontSize = 16.sp,
                color = LightOrange
            )
        }
    }
}
