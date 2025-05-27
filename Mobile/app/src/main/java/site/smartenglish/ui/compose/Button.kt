package site.smartenglish.ui.compose

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun WideButton(
    text: String,
    onClick: () -> Unit,
    width: Int = 356,
    height: Int = 56,
    fontsize: Int = 19,
    color: Color = Color.Unspecified
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .width(width.dp)
            .height(height.dp),
        content = {
            Text(
                text, fontSize = fontsize.sp, color = color
            )
        })
}


@Composable
fun BlurButton(
    buttonWidth: Dp = 200.dp,
    buttonHeight: Dp = 80.dp,
    offset: Pair<Dp, Dp> = Pair(0.dp, 0.dp),
    bitmap: Bitmap,
    blurRadius: Dp = 10.dp,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val imageBitmap = remember(bitmap) {
        bitmap.asImageBitmap()
    }

    val windowInfo = LocalWindowInfo.current
    val screenWidth = windowInfo.containerSize.width
    val screenHeight = windowInfo.containerSize.height

    val offsetModifier = Modifier
        .width(buttonWidth)
        .height(buttonHeight)
        .offset(x = offset.first, y = offset.second)

    Box(
        modifier = offsetModifier
            .blur(
                blurRadius, edgeTreatment = BlurredEdgeTreatment(RoundedCornerShape(12.dp))
            )

            .drawWithCache {
                val buttonPositionX = offset.first.toPx()
                val buttonPositionY = offset.second.toPx()

                // 计算背景图缩放比例
                val bitmapAspectRatio = bitmap.width.toFloat() / bitmap.height
                val screenAspectRatio = screenWidth / screenHeight

                val scaledWidth: Float
                val scaledHeight: Float
                val offsetX: Float
                val offsetY: Float
                // 计算实际显示的图像尺寸和偏移
                if (bitmapAspectRatio > screenAspectRatio) {
                    // 图像按高度缩放
                    val height = screenHeight
                    val width = height * bitmapAspectRatio
                    val xOffset = (width - screenWidth) / 2

                    scaledWidth = width
                    scaledHeight = height.toFloat()
                    offsetX = xOffset
                    offsetY = 0f
                } else {
                    // 图像按宽度缩放
                    val width = screenWidth
                    val height = width / bitmapAspectRatio
                    val yOffset = (height - screenHeight) / 2

                    scaledWidth = width.toFloat()
                    scaledHeight = height
                    offsetX = 0f
                    offsetY = yOffset

                }


                // 计算按钮区域对应的源图像区域
                val srcX = (buttonPositionX + offsetX) * bitmap.width / scaledWidth
                val srcY = (buttonPositionY + offsetY) * bitmap.height / scaledHeight
                val srcWidth = size.width * bitmap.width / scaledWidth
                val srcHeight = size.height * bitmap.height / scaledHeight

                onDrawWithContent {
                    drawImage(
                        image = imageBitmap,
                        srcOffset = IntOffset(srcX.toInt(), srcY.toInt()),
                        srcSize = IntSize(srcWidth.toInt(), srcHeight.toInt()),
                        dstOffset = IntOffset(0, 0),
                        dstSize = IntSize(size.width.toInt(), size.height.toInt())
                    )
                }
            }
            .clickable(true, onClick = onClick)

    )
    Box(
        modifier = offsetModifier.background(
            Color.White.copy(alpha = 0.1f),
            shape = RoundedCornerShape(12.dp)
        )
    ) {
        content()
    }

}