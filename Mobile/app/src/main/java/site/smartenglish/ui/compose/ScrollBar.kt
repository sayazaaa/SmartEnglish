package site.smartenglish.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import site.smartenglish.ui.theme.Orange

@Composable
fun ScrollBar(
    scrollPosition: () -> Float, scrollHeight: () -> Float, onScrollPositionChange: (Float) -> Unit
) {
    val scrollbarHeightRatio = scrollHeight().let {
        if (it == 0f) 0f else it.coerceIn(0.05f, 0.8f)
    }
    val maxOffsetRatio = 1f - scrollbarHeightRatio
    val offsetRatio = (scrollPosition() * maxOffsetRatio).coerceIn(0f, maxOffsetRatio)

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopEnd)
            .padding(end = 2.dp)
            .width(16.dp)
    ) {
        // 计算轨道可用高度
        val trackHeight = with(LocalDensity.current) {
            this@BoxWithConstraints.maxHeight.toPx()
        }
        var dragOffset by remember { mutableStateOf(0f) }


        Box(
            modifier = Modifier
                .fillMaxHeight(scrollbarHeightRatio)
                .width(12.dp)
                .align(Alignment.TopCenter)
                .offset(y = with(LocalDensity.current) { (offsetRatio * trackHeight).toDp() })
                .background(Orange, RoundedCornerShape(16.dp))
                .pointerInput(Unit) {

                    detectDragGestures(onDragStart = {
//                            dragOffset = offsetRatio * trackHeight
                    }, onDrag = { change, dragAmount ->
                        change.consume()
                        val newY = (dragOffset + dragAmount.y).coerceIn(0f, trackHeight)

                        // 计算新的滚动比例 (0f-1f)
                        val newScrollPosition =
                            if (trackHeight > 0) (newY / trackHeight) * maxOffsetRatio
                            else 0f

                        onScrollPositionChange(newScrollPosition.coerceIn(0f, 1f))
                        dragOffset = newY
                    })
                })
    }
}