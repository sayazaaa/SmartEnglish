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
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import site.smartenglish.ui.theme.DarkGrey
import site.smartenglish.ui.theme.Grey
import site.smartenglish.ui.viewmodel.AudioPlayerViewModel
import site.smartenglish.ui.viewmodel.DictationViewModel
import site.smartenglish.ui.viewmodel.UsingViewModel
import kotlin.time.Duration.Companion.seconds

@Composable
fun DictationScreen(
    navigationToList: (List<String>,Boolean) -> Unit,
    navigationBack: () -> Unit = {},
    dictationViewModel: DictationViewModel = hiltViewModel(),
    audioPlayerViewModel: AudioPlayerViewModel = hiltViewModel(),
    usingViewModel: UsingViewModel = hiltViewModel()
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
    val wordPlayedTime = dictationViewModel.currentWordPlayedTime.collectAsState().value
    val wordIndex = dictationViewModel.currentWordIndex.collectAsState().value
    val nWordBooks = dictationViewModel.NWordBookList.collectAsState().value
    val learnedWordCount = dictationViewModel.learnedWordCount.collectAsState().value
    val finished = dictationViewModel.finished.collectAsState().value

    var uiState by remember { mutableStateOf("setting") }
    var isPlaying by remember { mutableStateOf(false) }
    var wordPlayTime by remember { mutableIntStateOf(3) }
    var selectedNWordBookId by remember { mutableIntStateOf(0) }
    var wordSequence by remember { mutableStateOf("seq") }
    var autoNext by remember { mutableStateOf(true) }

    var wordSourceExpand by remember { mutableStateOf(true) }
    var wordPlayTimeExpand by remember { mutableStateOf(true) }
    var wordSequenceExpand by remember { mutableStateOf(true) }
    var selectNWordBook by remember { mutableStateOf(false) }

    val currentSoundUrl by rememberUpdatedState(soundUrl)
    val currentWordPlayedTime by rememberUpdatedState(wordPlayedTime)
    val currentWordPlayTime by rememberUpdatedState(wordPlayTime)
    val currentPlaying by rememberUpdatedState(isPlaying)
    val currentAutoNext by rememberUpdatedState(autoNext)

    val textColor=Color.White
    val darkTextColor = Color.White.copy(alpha = 0.7f)

    LaunchedEffect(Unit) {
        while(true) {
            delay(3.seconds)
            if(currentPlaying) {
                if(currentWordPlayedTime < currentWordPlayTime) {
                    dictationViewModel.wordPlayed()
                    audioPlayerViewModel.playAudio(currentSoundUrl)
                } else if (currentAutoNext) {
                    dictationViewModel.moveToNextWord()
                }
            }
        }
    }

    if(finished){
        navigationToList(words,true)
    }

    DisposableEffect(Unit) {
        onDispose {
            audioPlayerViewModel.stopAudio()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            usingViewModel.sendUsageTime("learn")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 44.dp)
            .background(DarkGrey)
    ) {
        // 顶部导航栏
        Row(
            modifier = Modifier.fillMaxWidth()
                .height(44.dp)
                .background(Grey),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { navigationBack()}) {
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
                IconButton(onClick = { uiState = "setting" }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "更多",
                        tint = textColor
                    )
                }
            }
        }
        // 单词信息区域
        if(uiState == "word"){
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(top = 82.dp, start = 30.dp)
            ) {
                Text(
                    text = words[wordIndex],
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
                                        audioPlayerViewModel.playAudio(soundUrl)
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
                            text = if(wordTypes.count()>index) wordTypes[index] else "",
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
        }
        if(uiState == "hide"){
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
                        imageVector = Icons.Filled.ImageNotSupported,
                        contentDescription = "播放读音",
                        tint = textColor,
                        modifier = Modifier
                            .size(64.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
        if (uiState == "setting") {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 69.dp, start = 17.dp, end = 17.dp)
            ) {
                //WordSource
                Card (
                    modifier = Modifier.width(394.dp)
                        .height(210.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if(wordSourceExpand) Color(0xFF24293D) else Color.Transparent
                    ),
                ){
                    Row(
                        modifier = Modifier.height(62.dp)
                            .fillMaxWidth()
                            .background( Color(0xFF292F45) )
                            .padding(start = 22.dp, end = 22.dp)
                            .clickable { wordSourceExpand = !wordSourceExpand },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "单词来源",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                        Icon(
                            imageVector = if (wordSourceExpand) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = "展开/收起",
                            tint = textColor
                        )
                    }
                    if (wordSourceExpand) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 36.dp, top=10.dp,end=36.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Card(
                                onClick = {dictationViewModel.fetchLearnedWords()},
                                modifier = Modifier.width(132.dp)
                                    .height(93.dp)
                                    .border(1.dp, Color.White, RoundedCornerShape(8.dp)),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF24293D)
                                ),
                            ) {
                                Box(
                                    modifier = Modifier.width(132.dp).height(64.dp)
                                        .padding(start = 32.dp)
                                ) {
                                    Text(
                                        text = "已学单词",
                                        fontSize = 16.sp,
                                        color = textColor,
                                        modifier = Modifier.padding(top= 18.dp)
                                    )
                                }
                            }
                            Card(
                                onClick = {
                                    selectNWordBook = true
                                },
                                modifier = Modifier.width(132.dp)
                                    .height(93.dp)
                                    .border(1.dp, Color.White, RoundedCornerShape(8.dp)),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF24293D)
                                ),
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize()
                                        .padding(start=32.dp,top= 32.dp)
                                ) {
                                    Text(
                                        text = "生词本",
                                        fontSize = 16.sp,
                                        color = textColor,
                                    )
                                    Icon(
                                        imageVector = Icons.Default.ChevronRight,
                                        contentDescription = "Enter",
                                        tint = textColor
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(25.dp))
                // WordPlayTime
                Card (
                    modifier = Modifier.width(394.dp)
                        .height(161.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if(wordPlayTimeExpand) Color(0xFF24293D) else Color.Transparent
                    ),
                ){
                    Row(
                        modifier = Modifier.height(62.dp)
                            .fillMaxWidth()
                            .background( Color(0xFF292F45) )
                            .padding(start = 22.dp, end = 22.dp)
                            .clickable{ wordPlayTimeExpand = !wordPlayTimeExpand},
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "单词播放次数",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                        Icon(
                            imageVector = if (wordPlayTimeExpand) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = "展开/收起",
                            tint = textColor
                        )
                    }
                    if (wordPlayTimeExpand) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 21.dp, top=10.dp,end=21.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Card(
                                onClick = {wordPlayTime = 1},
                                modifier = Modifier.width(97.dp)
                                    .height(39.dp)
                                    .border(1.dp, if (wordPlayTime == 1) Color.Cyan else Color.White, RoundedCornerShape(8.dp)),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF24293D)
                                ),
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = "1次",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textColor
                                    )
                                }
                            }
                            Card(
                                onClick = {wordPlayTime = 2},
                                modifier = Modifier.width(97.dp)
                                    .height(39.dp)
                                    .border(1.dp,if (wordPlayTime == 2) Color.Cyan else Color.White, RoundedCornerShape(8.dp)),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF24293D)
                                ),
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = "2次",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textColor
                                    )
                                }
                            }
                            Card(
                                onClick = {wordPlayTime = 3},
                                modifier = Modifier.width(97.dp)
                                    .height(39.dp)
                                    .border(1.dp,if (wordPlayTime == 3) Color.Cyan else Color.White, RoundedCornerShape(8.dp)),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF24293D)
                                ),
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = "3次",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textColor
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(25.dp))
                //WordSequence
                Card (
                    modifier = Modifier.width(394.dp)
                        .height(161.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if(wordSequenceExpand) Color(0xFF24293D) else Color.Transparent
                    ),
                ){
                    Row(
                        modifier = Modifier.height(62.dp)
                            .fillMaxWidth()
                            .background( Color(0xFF292F45) )
                            .padding(start = 22.dp, end = 22.dp)
                            .clickable{ wordSequenceExpand = !wordSequenceExpand},
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "听写顺序",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                        Icon(
                            imageVector = if (wordSequenceExpand) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = "展开/收起",
                            tint = textColor
                        )
                    }
                    if (wordSequenceExpand) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 21.dp, top=10.dp,end=21.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Card(
                                onClick = {wordSequence = "seq"},
                                modifier = Modifier.width(97.dp)
                                    .height(39.dp)
                                    .border(1.dp, if(wordSequence=="seq") Color.Cyan else Color.White, RoundedCornerShape(8.dp)),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF24293D)
                                ),
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = "顺序",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textColor
                                    )
                                }
                            }
                            Card(
                                onClick = {wordSequence = "qes"},
                                modifier = Modifier.width(97.dp)
                                    .height(39.dp)
                                    .border(1.dp, if(wordSequence=="qes") Color.Cyan else Color.White, RoundedCornerShape(8.dp)),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF24293D)
                                ),
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = "倒序",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textColor
                                    )
                                }
                            }
                            Card(
                                onClick = {wordSequence = "random"},
                                modifier = Modifier.width(97.dp)
                                    .height(39.dp)
                                    .border(1.dp,if(wordSequence=="random") Color.Cyan else Color.White, RoundedCornerShape(8.dp)),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF24293D)
                                ),
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = "乱序",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textColor
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(25.dp))
                Card (
                    modifier = Modifier.width(394.dp)
                        .height(62.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if(wordSequenceExpand) Color(0xFF24293D) else Color.Transparent
                    ),
                ){
                    Row(
                        modifier = Modifier.height(62.dp)
                            .fillMaxWidth()
                            .background( Color(0xFF292F45) )
                            .padding(start = 22.dp, end = 22.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "自动播放下一词",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                        Switch(
                            checked = autoNext,
                            onCheckedChange = { autoNext = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                uncheckedThumbColor = Color(0xFF9E9E9E)
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
                Card (
                    modifier = Modifier.width(342.dp)
                        .height(53.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF4A100)
                    ),
                    onClick = {
                        uiState = "hide"
                        isPlaying = true
                    }
                ){
                    Text(
                        text = "准备好纸笔，开始听写",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxSize()
                            .padding(top=12.dp)
                    )
                }
            }
        }
    }
    if (uiState=="hide" || uiState=="word") {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ){
            // 进度标识
            Text(
                text = "${wordIndex + 1}/${words.size}",
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
                IconButton(onClick = { navigationToList(words,false) }) {
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
                    if (isPlaying) {
                        dictationViewModel.wordPlayed()
                        audioPlayerViewModel.playAudio(soundUrl)
                    } else {
                        audioPlayerViewModel.stopAudio()
                    }
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
                    uiState = if (uiState == "word") "hide" else "word"
                }) {
                    Icon(
                        imageVector = if(uiState == "word") Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "显示/隐藏",
                        tint=textColor
                    )
                }
            }
        }
    }
    if (uiState=="setting" && selectNWordBook) {
        Box(
            modifier = Modifier.fillMaxSize()
                .background(Color(0x80000000)),
            contentAlignment = Alignment.BottomCenter
        ){
            Card(
                modifier = Modifier.fillMaxWidth()
                    .height(400.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF24293D)
                ),
            ) {
                Spacer(modifier = Modifier.height(37.dp))
                Text(
                    text = "选择生词本",
                    fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = textColor
                )
                Column(
                    modifier = Modifier.fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                )  {
                    nWordBooks.forEach { nWordBook ->
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(16.dp)
                                .clickable {
                                    selectedNWordBookId = nWordBook.id?:0
                                    dictationViewModel.fetchNWordBookWords(nWordBook.id?:0)
                                    selectNWordBook = false
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = nWordBook.name?:"未命名生词本",
                                fontSize = 16.sp,
                                color = textColor
                            )
                            Icon(
                                imageVector = if (selectedNWordBookId == nWordBook.id) Icons.Default.Check else Icons.Default.CheckBoxOutlineBlank,
                                contentDescription = "已选择",
                                tint = if (selectedNWordBookId == nWordBook.id) Color(0xFFF4A100) else Color(0xFF878278)
                            )
                        }
                    }
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun DictationScreenPreview() {
//    DictationScreen()
//}