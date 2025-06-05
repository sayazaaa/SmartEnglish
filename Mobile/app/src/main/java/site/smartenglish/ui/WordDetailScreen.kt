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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import site.smartenglish.ui.viewmodel.BackgroundImageViewmodel

@Composable
fun WordDetailScreen(
    bgViewmodel: BackgroundImageViewmodel = hiltViewModel()
) {
    //input data
    val soundType = "美"
    val exampleSentence="111Test"
    val symbol="/nˈzɜːrt/"
    val exampleSentenceTranslate="Test111"
    val word="resort"
    val wordTypes = listOf(
        "vi.",
        "vt.",
        "vt.",
        "n."
    )
    val wordMeanings = listOf(
        "111",
        "222",
        "333",
        "444"
    )
    val phrases = listOf(
        "a ski resort",
        "a holiday resort",
        "a last resort"
    )
    val phraseTranslate = listOf(
        "111",
        "Test",
        "Genshin"
    )
    val usages=listOf(
        "resort to stl",
        "test"
    )
    val usageTranslate=listOf(
        "111",
        "Test"
    )


    //config
    val bitmap = bgViewmodel.backgroundBitmap.collectAsState().value
    val textColor = Color.White
    val darkTextColor = Color.LightGray

    var phraseMode by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .blur(32.dp)
    ) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 页面角落辅助功能组件
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "返回",
                tint = textColor,
                modifier = Modifier.size(24.dp)
            )
            Row {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "收藏",
                    tint = textColor,
                    modifier = Modifier.size(24.dp)
                )
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "删除",
                    tint = textColor,
                    modifier = Modifier.size(24.dp)
                )
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "更多选项",
                    tint = textColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(37.dp))
        // 单词展示区
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = word,
                color = textColor,
                fontSize = 46.sp,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    modifier = Modifier
                        .width(38.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.5f))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = soundType,
                            color = Color.LightGray,
                            fontSize = 18.sp,
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "播放读音",
                            tint = textColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = symbol,
                    color = Color.LightGray,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Column {
                wordMeanings.forEachIndexed { index, option ->
                    Row(
                        modifier = Modifier
                            .padding(start = 18.dp)
                    ) {
                        Text(
                            text = wordTypes[index],
                            color = darkTextColor,
                            fontSize = 17.sp
                        )
                        Text(
                            text = option,
                            color = textColor,
                            fontSize = 18.sp
                        )
                    }
                }
            }
            // 例句
            Spacer(modifier = Modifier.height(25.dp))
            Card(
                modifier = Modifier
                    .height(126.dp)
                    .width(394.dp)
                    .padding(start = 17.dp, end = 17.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.5f))
            ) {
                Text(
                    text = exampleSentence,
                    color = darkTextColor,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 18.dp, end = 18.dp, top = 18.dp)
                )
                Text(
                    text = exampleSentenceTranslate,
                    color = darkTextColor,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 18.dp, end = 18.dp)
                )
            }
            Spacer(modifier=Modifier.height(12.dp))
            Card(
                modifier = Modifier
                    .height(310.dp)
                    .width(394.dp)
                    .padding(start = 17.dp, end = 17.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.5f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ){
                    Column(
                        modifier = Modifier
                            .padding(18.dp)
                    ){
                        phrases.forEachIndexed{ index, option ->
                            Row{
                                Text(
                                    text = option,
                                    color = darkTextColor,
                                    fontSize = 17.sp
                                )
                                Text(
                                    text = phraseTranslate[index],
                                    color = darkTextColor,
                                    fontSize = 15.sp,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        usages.forEachIndexed{ index, option ->
                            Row {
                                Text(
                                    text = option,
                                    color = textColor,
                                    fontSize = 17.sp
                                )
                                Text(
                                    text = usageTranslate[index],
                                    color = darkTextColor,
                                    fontSize = 15.sp,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(8.dp)
                            .align(Alignment.BottomStart)
                    ){
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (phraseMode) Color(0xFFDA9209) else Color.White.copy(alpha = 0.2f)
                            ),
                            modifier = Modifier
                                .width(68.dp)
                                .height(32.dp)
                                .clickable { phraseMode = true }
                                .border(
                                    width = 2.dp,
                                    color = if (phraseMode) Color.Transparent else Color.White.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Text(
                                text = "词组搭配",
                                color =textColor ,
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (!phraseMode) Color(0xFFDA9209) else Color.White.copy(alpha = 0.2f)
                            ),
                            modifier = Modifier
                                .width(68.dp)
                                .height(32.dp)
                                .clickable { phraseMode = false }
                                .border(
                                    width = 2.dp,
                                    color = if (!phraseMode) Color.Transparent else Color.White.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Text(
                                text = "词汇变形",
                                color =textColor ,
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 37.dp)
        ) {
            Button(
                onClick = {
                    //TODO next word
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.3f),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(32.dp),
                modifier =Modifier.width(105.dp)
            ) {
                Text(
                    text = "下一词",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold)
            }
        }
    }
}

//@Preview
//@Composable
//fun WordDetailScreenPreview() {
//    WordDetailScreen()
//}