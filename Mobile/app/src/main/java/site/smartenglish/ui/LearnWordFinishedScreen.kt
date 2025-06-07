package site.smartenglish.ui

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import site.smartenglish.ui.theme.LightGrey
import site.smartenglish.ui.viewmodel.BackgroundImageViewmodel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnWordFinishedScreen(
    words: List<String> = emptyList(),
    navigationToHome: () -> Unit = {},
    navigationToLearn:() -> Unit = {},
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
                            modifier = Modifier.padding(start = 17.dp).align(Alignment.CenterStart),
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
                    modifier = Modifier.width(105.dp)
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
                        navigationToLearn()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.3f),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(64.dp),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.width(105.dp)
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