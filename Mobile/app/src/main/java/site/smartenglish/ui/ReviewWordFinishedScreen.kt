package site.smartenglish.ui

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import site.smartenglish.ui.theme.LightGrey
import site.smartenglish.ui.viewmodel.BackgroundImageViewmodel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewWordFinishedScreen(
    words: List<String> = emptyList(),
    navigationToHome: () -> Unit = {},
    navigationToReview: () -> Unit = {},
    bgViewmodel: BackgroundImageViewmodel = hiltViewModel(LocalActivity.current as ViewModelStoreOwner)
) {
    //config
    val bitmap = bgViewmodel.backgroundBitmap.collectAsState().value
    val textColor = Color.White
    val darkTextColor = Color.LightGray


    // 背景图片容器
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 图片层
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(100.dp)
        ) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        // 黑色蒙层
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.3f))
        )
    }

    Scaffold(
        containerColor = Color.Transparent,
        contentColor = Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("学习完成", color = textColor, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navigationToHome() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "返回",
                            tint = textColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = textColor,
                    navigationIconContentColor = textColor
                )
            )
        }
    ) { inn ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(inn)
        ) {
            Column {
                Text(
                    "已学单词列表",
                    fontSize = 21.sp,
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 17.dp, start = 13.dp, bottom = 11.dp)
                )
                HorizontalDivider(
                    color = LightGrey,
                )
                words.forEach {
                    Box(modifier = Modifier.height(50.dp)) {
                        Text(
                            text = it,
                            fontSize = 18.sp,
                            color = textColor,
                            modifier = Modifier
                                .padding(start = 17.dp)
                                .align(Alignment.CenterStart),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            HorizontalDivider(
                color = darkTextColor,
                thickness = 1.dp,
                modifier = Modifier
                    .width(426.dp)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 159.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 89.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        navigationToHome()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.3f),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(64.dp),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .width(105.dp)
                        .height(44.dp),
                ) {
                    Text(
                        text = "休息一下",
                        fontSize = 16.sp,
                        overflow = TextOverflow.Visible,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
                Spacer(modifier = Modifier.width(95.dp))
                Button(
                    onClick = {
                        navigationToReview()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.3f),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(64.dp),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .width(105.dp)
                        .height(44.dp),
                ) {
                    Text(
                        text = "再来一组",
                        fontSize = 16.sp,
                        overflow = TextOverflow.Visible,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
            }
        }
    }
}