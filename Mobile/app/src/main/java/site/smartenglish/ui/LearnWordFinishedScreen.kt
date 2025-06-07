package site.smartenglish.ui

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
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import site.smartenglish.ui.viewmodel.BackgroundImageViewmodel

@Composable
fun LearnWordFinishedScreen(
    bgViewmodel: BackgroundImageViewmodel = hiltViewModel()
) {
    //input data
    val words = listOf(
        "test",
        "zzz",
        "genshin",
        "hsr"
    )


    //config
    val bitmap = bgViewmodel.backgroundBitmap.collectAsState().value
    val textColor = Color.White
    val darkTextColor = Color.LightGray

    Box(
        modifier = Modifier
            .fillMaxSize()
            .blur(64.dp)
    ) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
    Column{
        Spacer(modifier = Modifier.height(44.dp))
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "返回",
                tint = textColor,
                modifier = Modifier.size(24.dp)
                    .align(Alignment.CenterStart)
            )
            Text(
                text = "学习完成",
                fontSize = 24.sp,
                color = textColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "单词列表",
                fontSize = 21.sp,
                color = textColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            HorizontalDivider(
                color = darkTextColor,
                thickness = 1.dp,
                modifier = Modifier.padding(bottom = 16.dp)
                    .width(426.dp)
            )
            words.forEach { word ->
                Text(
                    text = word,
                    fontSize = 18.sp,
                    color = textColor,
                )
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        HorizontalDivider(
            color = darkTextColor,
            thickness = 1.dp,
            modifier = Modifier
                .width(426.dp)
                .align(Alignment.BottomCenter)
                .padding(bottom=159.dp)
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
                    //TODO 摆
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.3f),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(64.dp),
                contentPadding = PaddingValues(0.dp),
                modifier =Modifier.width(105.dp)
                    .height(44.dp),
            ) {
                Text(
                    text = "休息一下",
                    fontSize = 16.sp,
                    overflow = TextOverflow.Visible,
                    textAlign = TextAlign.Center,
                    maxLines = 1)
            }
            Spacer(modifier = Modifier.width(95.dp))
            Button(
                onClick = {
                    //TODO 卷
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.3f),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(64.dp),
                contentPadding = PaddingValues(0.dp),
                modifier =Modifier.width(105.dp)
                    .height(44.dp),
            ) {
                Text(
                    text = "再来一组",
                    fontSize = 16.sp,
                    overflow = TextOverflow.Visible,
                    textAlign = TextAlign.Center,
                    maxLines = 1)
            }
        }
    }
}

//@Preview
//@Composable
//fun LearnWordFinishScreenPreview() {
//    LearnWordFinishedScreen()
//}