package site.smartenglish.ui.compose

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.TabPosition
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

private val TabRowIndicatorSpec: AnimationSpec<Dp> =
    tween(durationMillis = 250, easing = FastOutSlowInEasing)

fun Modifier.tabIndicatorOffset(currentTabPosition: TabPosition): Modifier =
    composed(
        inspectorInfo =
            debugInspectorInfo {
                name = "tabIndicatorOffset"
                value = currentTabPosition
            }
    ) {
        val currentTabWidth by
        animateDpAsState(
            targetValue = currentTabPosition.width,
            animationSpec = TabRowIndicatorSpec
        )
        val indicatorOffset by
        animateDpAsState(
            targetValue = currentTabPosition.left,
            animationSpec = TabRowIndicatorSpec
        )
        wrapContentSize(Alignment.BottomStart)
            .offset { IntOffset(x = indicatorOffset.roundToPx() + currentTabWidth.roundToPx() / 2 - 10.dp.roundToPx(), y = 0) }
            .width(20.dp)
    }