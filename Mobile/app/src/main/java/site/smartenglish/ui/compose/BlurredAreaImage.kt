package site.smartenglish.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun BlurredAreaImage(
    imageRes: Int,
    blurRegion: Rect,
    blurRadius: Dp = 10.dp,
    overlayColor: Color = Color.White.copy(alpha = 0.2f)
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // 原始背景图片
        AsyncImage(
            model = imageRes,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 模糊区域 - 使用相同图片的副本但只显示特定区域并应用模糊
        with(LocalDensity.current) {
            Box(
                modifier = Modifier
                    .offset(
                        x = blurRegion.left.toDp(),
                        y = blurRegion.top.toDp()
                    )
                    .size(
                        width = blurRegion.width.toDp(),
                        height = blurRegion.height.toDp()
                    )
                    .clip(RectangleShape)
            ) {
                // 同一张图片，但偏移位置让它与原图对齐
                AsyncImage(
                    model = imageRes,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            translationX = -blurRegion.left
                            translationY = -blurRegion.top
                            scaleX = size.width / blurRegion.width
                            scaleY = size.height / blurRegion.height
                        }
                        .blur(radius = blurRadius)
                )

                // 半透明覆盖层
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(overlayColor)
                )
            }
        }
    }
}