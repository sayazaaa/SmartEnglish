package site.smartenglish.ui.compose

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import site.smartenglish.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenterAlignedBackArrowTopAppBar(
    title: String,
    onBackClick: () -> Unit = {},
    actions: @Composable() (RowScope.() -> Unit) = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                color = White,
                fontSize = 17.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                    contentDescription = "返回",
                    tint = White
                )
            }
        },
        actions = actions,
    )
}
