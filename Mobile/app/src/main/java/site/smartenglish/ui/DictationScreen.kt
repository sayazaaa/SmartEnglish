package site.smartenglish.ui

import android.graphics.Paint.Align
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import site.smartenglish.ui.viewmodel.DictationViewModel

@Composable
fun DictationScreen(
    dictationViewModel: DictationViewModel = hiltViewModel()
) {
    // 单词数据
    val words = dictationViewModel.currentWords.collectAsState().value
    val currentWord= dictationViewModel.currentWord
    val symbol = dictationViewModel.currentSymbol.collectAsState().value
    val favourite = dictationViewModel.isCurrentFavourite.collectAsState().value
    val meanings = dictationViewModel.currentMeanings.collectAsState().value
    val wordTypes = dictationViewModel.currentWordType.collectAsState().value
    val soundType = dictationViewModel.currentSoundType.collectAsState().value
    val soundUrl = dictationViewModel.currentSoundUrl.collectAsState().value

    var currentWordIndex by remember { mutableIntStateOf(0) }
    var isWordVisible by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }

    val textColor=Color.White
    val darkTextColor = Color.White.copy(alpha = 0.7f)

    Row(
        modifier = Modifier.fillMaxWidth()
            .background(Color(0xFF292F45)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = { /* TODO 返回逻辑 */ }) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "返回",
                tint = textColor
            )
        }

        Text(text = "听写", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textColor)

        Row {
            IconButton(onClick = { dictationViewModel.switchWordInNWordBook(currentWord) }) {
                Icon(
                    imageVector = if (favourite) Icons.Filled.Star else Icons.Filled.StarOutline,
                    contentDescription = "收藏",
                    tint = textColor
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { /* TODO 设置逻辑 */ }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "更多",
                    tint = textColor
                )
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // 单词信息区域
        if(isWordVisible){
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(top = 82.dp, start = 30.dp)
            ) {
                Text(
                    text = words[currentWordIndex],
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Card(
                        modifier = Modifier
                            .width(38.dp)
                            .height(20.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.5f))
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ){
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = soundType,
                                    color = Color.LightGray,
                                    fontSize = 14.sp,
                                )
                                IconButton(
                                    onClick = {
                                        // TODO 播放读音逻辑
                                    },
                                    modifier = Modifier.padding(0.dp, 0.dp, 4.dp, 0.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                        contentDescription = "播放读音",
                                        tint = textColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier=Modifier.width(10.dp))
                    Text(
                        text = symbol,
                        fontSize = 14.sp,
                        color = darkTextColor,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                meanings.forEachIndexed { index, option ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Row {
                        Text(
                            text = wordTypes[index],
                            color = darkTextColor,
                            fontSize = 17.sp
                        )
                        Text(
                            text = option,
                            color = textColor,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }else{
            Card(
                modifier = Modifier.align(Alignment.Center)
                    .width(264.dp)
                    .height(264.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF24293D)
                ),
            ) {
                Box(modifier=Modifier.fillMaxSize().align(Alignment.CenterHorizontally)){
                    Icon(
                        imageVector = Icons.Filled.ImageNotSupported,//TODO 替换图标
                        contentDescription = "播放读音",
                        tint = textColor,
                        modifier = Modifier
                            .size(64.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ){
        // 进度标识
        Text(
            text = "${currentWordIndex + 1}/${words.size}",
            fontSize = 16.sp,
            color = textColor,
            modifier = Modifier.padding(bottom = 117.dp)
        )
        // 底部控制栏
        Row(
            modifier = Modifier.fillMaxWidth()
                .background(Color(0xFF292F45))
                .fillMaxWidth()
                .height(95.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* 打开菜单 */ }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    tint=textColor,
                    contentDescription = "菜单"
                )
            }
            IconButton(onClick = {
                dictationViewModel.moveToLastWord()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    tint=textColor,
                    contentDescription = "上一词"
                )
            }
            IconButton(onClick = {
                isPlaying = !isPlaying
                /* TODO 具体播放逻辑 */
            }) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "播放",
                    tint=textColor
                )
            }
            IconButton(onClick = {
                dictationViewModel.moveToNextWord()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    tint=textColor,
                    contentDescription = "下一词"
                )
            }
            IconButton(onClick = {
                isWordVisible = !isWordVisible
            }) {
                Icon(
                    imageVector = if(!isWordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = "显示/隐藏",
                    tint=textColor
                )
            }
        }
    }
}

//@Preview
//@Composable
//fun DictationScreenPreview() {
//    DictationScreen()
//}