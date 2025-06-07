package site.smartenglish.ui

import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import kotlinx.coroutines.delay
import site.smartenglish.R
import site.smartenglish.ui.theme.LightGrey
import site.smartenglish.ui.theme.White
import site.smartenglish.ui.viewmodel.BackgroundImageViewmodel
import site.smartenglish.ui.viewmodel.ReviewViewmodel
import site.smartenglish.ui.viewmodel.ReviewWordInfo
import site.smartenglish.ui.viewmodel.SnackBarViewmodel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewWordScreen(
    navigateToReviewWordFinished: (
        wordList: List<String>
    ) -> Unit = {},
    navigateBack: () -> Unit = {},
    reviewViewmodel: ReviewViewmodel = hiltViewModel(),
    bgViewmodel: BackgroundImageViewmodel = hiltViewModel(),
    snackBarViewmodel: SnackBarViewmodel = hiltViewModel(LocalActivity.current as ViewModelStoreOwner)
) {
    val bitmap = bgViewmodel.backgroundBitmap.collectAsState().value
    val wordDetail = reviewViewmodel.wordDetail.collectAsState().value
    val reviewedNum = reviewViewmodel.reviewedWordNum.collectAsState().value
    val isLoading = reviewViewmodel.isLoading.collectAsState().value
    val navigateBackSelection = reviewViewmodel.navigateBackSelection.collectAsState().value
    val navigateToFinished = reviewViewmodel.navigateToFinish.collectAsState().value
    val snackBar = reviewViewmodel.snackBar.collectAsState().value

    var showDetailScreen by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        reviewViewmodel.getWordSetDetail()
    }

    LaunchedEffect(navigateBackSelection) {
        if (navigateBackSelection) {
            navigateBack()
        }
    }

    LaunchedEffect(navigateToFinished) {
        if (navigateToFinished) {
            // 获取已学单词列表
            val reviewedWords = reviewViewmodel.getReviewedWordList()
            // 导航到学习完成页面
            navigateToReviewWordFinished(reviewedWords)
        }
    }

    LaunchedEffect(snackBar) {
        if (snackBar.isNotEmpty()) {
            snackBarViewmodel.showSnackbar(snackBar)
            reviewViewmodel.clearSnackBar() // 清除 snackbar 内容
        }
    }

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
        containerColor = Color.Transparent, topBar = {
            TopAppBar(
                navigationIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        IconButton(
                            onClick =
                                {
                                    navigateBack()
                                }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "返回",
                                tint = LightGrey,
                                modifier = Modifier
                                    .padding(start = 10.dp)
                                    .size(32.dp)
                            )
                        }
                        Text(
                            "${reviewedNum}/10",
                            color = LightGrey,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(start = 10.dp)
                        )
                    }
                }, actions = {
                    IconButton(
                        onClick = {
                            //TODO
                        }) {
                        Icon(
                            painter = painterResource(R.drawable.kid_star),
                            contentDescription = "收藏",
                            tint = LightGrey,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }, title = {}, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent, scrolledContainerColor = Color.Transparent
                )
            )
        })
    { padding ->
        if (isLoading) {
            // 显示加载指示器
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = White)
            }
        } else if (wordDetail == ReviewWordInfo()) {
            // 显示空状态
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "没有可用的单词数据",
                    color = White,
                    fontSize = 18.sp
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 17.dp, top = 37.dp, end = 17.dp)
                    .padding(padding)
                    .background(Color.Transparent)
            ) {
                if (!showDetailScreen) {
                    val onCorrect = {
                        reviewViewmodel.passWord()
                        showDetailScreen = true
                    }
                    val onWrong = {
                        reviewViewmodel.failWord()
                        showDetailScreen = true
                    }
                    when (wordDetail.stage) {
                        0 -> {
                            // 阶段0：给英文选中文
                            Stage0(wordDetail, onCorrect, onWrong)
                        }

                        1 -> {
                            // 阶段1：给中文选英文
                            Stage1(wordDetail, onCorrect, onWrong)
                        }

                        2 -> {
                            Stage2(wordDetail, onCorrect, onWrong)
                        }

                        3 -> {
                            Stage3(wordDetail, onCorrect, onWrong)
                        }

                        else -> {
                            Log.e("ReviewWordScreen", "未知阶段: ${wordDetail.stage}")
                            reviewViewmodel.nextWord()
                        }
                    }
                } else {
                    // detail screen
                    // 单词展示区
                    Column(
                        modifier = Modifier.padding(start = 13.dp)
                    ) {
                        Text(
                            text = wordDetail.word.word ?: "",
                            color = White,
                            fontSize = 46.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // 读音
                            Row(
                                modifier = Modifier
                                    .background(
                                        color = Color.White.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(100.dp)
                                    )
                                    .padding(horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = "美",
                                    color = LightGrey,
                                    fontSize = 11.sp,
                                )
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                    contentDescription = "播放读音",
                                    tint = LightGrey,
                                    modifier = Modifier.size(10.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            // 音标
                            Text(
                                text = wordDetail.word.phonetic ?: "",
                                color = LightGrey,
                                fontSize = 14.sp,
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        wordDetail.word.explanations?.forEach {
                            val parts = it.split(".")
                            Row(
                                verticalAlignment = Alignment.Bottom,
                            ) {
                                Text(
                                    text = "${parts[0]}.",
                                    color = LightGrey,
                                    fontSize = 16.sp,
                                )
                                if (
                                    parts.size > 1
                                ) {
                                    Text(
                                        text = parts[1],
                                        color = White,
                                        fontSize = 16.sp,
                                    )
                                }
                            }
                            Spacer(Modifier.height(12.dp))
                        }
                    }
                    Spacer(Modifier.height(14.dp))

                    // 判断各部分是否有数据
                    val hasExamples =
                        !wordDetail.word.examples.isNullOrEmpty()
                    val hasSynonyms =
                        !(wordDetail.word.synonyms?.a.isNullOrEmpty() &&
                                wordDetail.word.synonyms?.v.isNullOrEmpty() &&
                                wordDetail.word.synonyms?.n.isNullOrEmpty())
                    val hasAntonyms =
                        !(wordDetail.word.antonyms?.a.isNullOrEmpty() &&
                                wordDetail.word.antonyms?.v.isNullOrEmpty() &&
                                wordDetail.word.antonyms?.n.isNullOrEmpty())

                    // 根据有数据的部分创建tab列表
                    val availableTabs = mutableListOf<String>()
                    if (hasExamples) availableTabs.add("例句")
                    if (hasSynonyms) availableTabs.add("同义词")
                    if (hasAntonyms) availableTabs.add("反义词")

                    var selectedTabIndex by remember { mutableIntStateOf(0) }
                    val scrollState = rememberScrollState()

                    if (availableTabs.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .background(Color.White.copy(0.1f), RoundedCornerShape(10.dp))
                                .verticalScroll(scrollState)
                        ) {
                            // 顶部Tab栏
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                availableTabs.forEachIndexed { index, title ->
                                    Box(
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp)
                                            .weight(1f)
                                            .clip(RoundedCornerShape(10.dp))
                                            .clickable { selectedTabIndex = index }
                                    ) {
                                        Column(
                                            modifier = Modifier.align(Alignment.Center),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                        ) {
                                            Text(
                                                text = title,
                                                color = if (selectedTabIndex == index) White else LightGrey,
                                                fontSize = 14.sp,
                                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                            )
                                            AnimatedVisibility(
                                                visible = selectedTabIndex == index,
                                                enter = fadeIn() +
                                                        expandVertically(),
                                                exit = fadeOut() +
                                                        shrinkVertically()
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .padding(top = 2.dp)
                                                        .width(20.dp)
                                                        .height(2.dp)
                                                        .background(
                                                            White,
                                                            RoundedCornerShape(1.dp)
                                                        )
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // 内容区域
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 18.dp, vertical = 12.dp)
                            ) {
                                AnimatedContent(
                                    targetState = selectedTabIndex,
                                    transitionSpec = {
                                        val slideDirection = if (targetState > initialState) {
                                            fadeIn() togetherWith fadeOut()
                                        } else {
                                            fadeIn() togetherWith fadeOut()
                                        }
                                        slideDirection.using(
                                            SizeTransform(clip = false)
                                        )
                                    },
                                    label = "TabContent"
                                ) { tabIndex ->
                                    Column {
                                        when (availableTabs[tabIndex]) {
                                            "例句" -> { // 例句
                                                Column {
                                                    wordDetail.word.examples?.forEach { example ->
                                                        Text(
                                                            text = example?.english ?: "",
                                                            color = White,
                                                            fontSize = 15.sp,
                                                        )
                                                        Text(
                                                            text = example?.chinese ?: "",
                                                            color = LightGrey,
                                                            fontSize = 12.sp,
                                                            modifier = Modifier.padding(bottom = 16.dp)
                                                        )
                                                    }
                                                }
                                            }

                                            "同义词" -> { // 同义词
                                                if (wordDetail.word.synonyms?.a?.isNotEmpty() == true) {
                                                    Text(
                                                        text = "a.",
                                                        color = White,
                                                        fontSize = 16.sp,
                                                        modifier = Modifier.padding(vertical = 2.dp),
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    wordDetail.word.synonyms?.a?.forEach { synonym ->
                                                        Text(
                                                            text = synonym,
                                                            color = White,
                                                            fontSize = 15.sp,
                                                            modifier = Modifier.padding(
                                                                vertical = 2.dp,
                                                                horizontal = 16.dp
                                                            )
                                                        )
                                                    }
                                                }

                                                if (wordDetail.word.synonyms?.v?.isNotEmpty() == true) {
                                                    Text(
                                                        text = "v.",
                                                        color = White,
                                                        fontSize = 16.sp,
                                                        modifier = Modifier.padding(vertical = 2.dp),
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    wordDetail.word.synonyms?.v?.forEach { synonym ->
                                                        Text(
                                                            text = synonym,
                                                            color = White,
                                                            fontSize = 15.sp,
                                                            modifier = Modifier.padding(
                                                                vertical = 2.dp,
                                                                horizontal = 16.dp
                                                            )
                                                        )
                                                    }
                                                }

                                                if (wordDetail.word.synonyms?.n?.isNotEmpty() == true) {
                                                    Text(
                                                        text = "n.",
                                                        color = White,
                                                        fontSize = 16.sp,
                                                        modifier = Modifier.padding(vertical = 2.dp),
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    wordDetail.word.synonyms?.n?.forEach { synonym ->
                                                        Text(
                                                            text = synonym,
                                                            color = White,
                                                            fontSize = 15.sp,
                                                            modifier = Modifier.padding(
                                                                vertical = 2.dp,
                                                                horizontal = 16.dp
                                                            )
                                                        )
                                                    }
                                                }
                                            }

                                            "反义词" -> { // 反义词
                                                if (wordDetail.word.antonyms?.a?.isNotEmpty() == true) {
                                                    Text(
                                                        text = "a.",
                                                        color = White,
                                                        fontSize = 16.sp,
                                                        modifier = Modifier.padding(vertical = 2.dp),
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    wordDetail.word.antonyms?.a?.forEach { antonym ->
                                                        Text(
                                                            text = antonym,
                                                            color = White,
                                                            fontSize = 15.sp,
                                                            modifier = Modifier.padding(
                                                                vertical = 2.dp,
                                                                horizontal = 16.dp
                                                            )
                                                        )
                                                    }
                                                }

                                                if (wordDetail.word.antonyms?.v?.isNotEmpty() == true) {
                                                    Text(
                                                        text = "v.",
                                                        color = White,
                                                        fontSize = 16.sp,
                                                        modifier = Modifier.padding(vertical = 2.dp),
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    wordDetail.word.antonyms?.v?.forEach { antonym ->
                                                        Text(
                                                            text = antonym,
                                                            color = White,
                                                            fontSize = 15.sp,
                                                            modifier = Modifier.padding(
                                                                vertical = 2.dp,
                                                                horizontal = 16.dp
                                                            )
                                                        )
                                                    }
                                                }

                                                if (wordDetail.word.antonyms?.n?.isNotEmpty() == true) {
                                                    Text(
                                                        text = "n.",
                                                        color = White,
                                                        fontSize = 16.sp,
                                                        modifier = Modifier.padding(vertical = 2.dp),
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    wordDetail.word.antonyms?.n?.forEach { antonym ->
                                                        Text(
                                                            text = antonym,
                                                            color = White,
                                                            fontSize = 15.sp,
                                                            modifier = Modifier.padding(
                                                                vertical = 2.dp,
                                                                horizontal = 16.dp
                                                            )
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        // 没有可用的标签时显示提示
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .background(Color.White.copy(0.1f), RoundedCornerShape(10.dp))
                        ) {
                            Text(
                                text = "没有可用的内容",
                                color = LightGrey,
                                fontSize = 16.sp
                            )
                        }
                    }
                    //底部按钮
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 37.dp, bottom = 89.dp)
                    ) {
                        Button(
                            onClick = {
                                showDetailScreen = false
                                // 继续学习下一个单词
                                reviewViewmodel.nextWord()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White.copy(alpha = 0.3f),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(32.dp),
                            modifier = Modifier.width(105.dp)
                        ) {
                            Text(
                                text = "下一词", fontSize = 16.sp
                            )
                        }
                    }
                }

            }
        }
    }
}

@Composable
private fun ColumnScope.Stage0(
    wordDetail: ReviewWordInfo,
    onCorrect: () -> Unit,
    onWrong: () -> Unit
) {
    // 用于跟踪选中的选项和反馈状态
    var selectedOptionIndex by remember { mutableIntStateOf(-1) }
    var isCorrect by remember { mutableStateOf(false) }
    var showFeedback by remember { mutableStateOf(false) }

    // 正确的选项索引
    val correctIndex = wordDetail.similarWords.indexOfFirst {
        it.word == wordDetail.word.word
    }
    Log.d(
        "Stage0",
        "Current word: ${wordDetail.word.word}, Correct index: $correctIndex"
    )

    // 启动反馈效果后延迟执行
    LaunchedEffect(showFeedback) {
        if (showFeedback) {
            delay(1000) // 显示反馈效果1秒
            if (isCorrect) {
                onCorrect()
            } else {
                onWrong()
            }
        }
    }

    // 单词展示区
    Column(
        modifier = Modifier.padding(start = 13.dp)
    ) {
        Text(
            text = wordDetail.word.word ?: "",
            color = White,
            fontSize = 46.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // 读音
            Row(
                modifier = Modifier
                    .background(
                        color = Color.White.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(100.dp)
                    )
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "美",
                    color = LightGrey,
                    fontSize = 11.sp,
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = "播放读音",
                    tint = LightGrey,
                    modifier = Modifier.size(10.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            // 音标
            Text(
                text = wordDetail.word.phonetic ?: "",
                color = LightGrey,
                fontSize = 14.sp,
            )
        }
    }

    Spacer(modifier = Modifier.height(20.dp))
    Text(
        text = "请从下列 4 个选项中选择正确词义",
        color = LightGrey,
        fontSize = 13.sp,
    )

    wordDetail.similarWords.forEachIndexed { index, option ->
        Spacer(modifier = Modifier.height(13.dp))

        Box(
            modifier = Modifier
                .clickable(enabled = !showFeedback) {
                    selectedOptionIndex = index
                    isCorrect = (index == correctIndex)
                    showFeedback = true
                }
                .height(80.dp)
                .fillMaxWidth()
                .background(Color.White.copy(0.1f), RoundedCornerShape(10.dp))
        ) {
            // 选项内容
            Column(
                modifier = Modifier.padding(18.dp)
            ) {
                option.explanations?.forEach {
                    val part = it.split(".")
                    Row {
                        Text(
                            text = "${part[0]}.",
                            color = White,
                            fontSize = 16.sp,
                        )
                        if (
                            part.size > 1
                        ) {
                            Text(
                                text = part[1],
                                color = LightGrey,
                                fontSize = 16.sp,
                            )
                        }
                    }
                }
            }

            // 反馈覆盖层
            if (showFeedback) {
                val feedbackColor = when {
                    index == correctIndex -> Color.Green.copy(alpha = 0.2f)
                    else -> Color.Red.copy(alpha = 0.2f)
                }
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(feedbackColor, RoundedCornerShape(10.dp))
                )
            }
        }
    }

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 93.dp)
    ) {
        Button(
            onClick = {
                // "看答案"按钮点击等同于选错
                selectedOptionIndex = -1  // 没有选中任何选项
                isCorrect = false         // 标记为错误
                showFeedback = true       // 显示反馈
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White.copy(alpha = 0.3f),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier.width(105.dp),
            enabled = !showFeedback
        ) {
            Text(
                text = "看答案", fontSize = 16.sp
            )
        }
    }
}


@Composable
private fun ColumnScope.Stage1(
    wordDetail: ReviewWordInfo,
    onCorrect: () -> Unit,
    onWrong: () -> Unit
) {
    // 用于跟踪选中的选项和反馈状态
    var selectedOptionIndex by remember { mutableIntStateOf(-1) }
    var isCorrect by remember { mutableStateOf(false) }
    var showFeedback by remember { mutableStateOf(false) }

    // 正确的选项索引
    val correctIndex = wordDetail.similarWords.indexOfFirst {
        it.word == wordDetail.word.word
    }
    Log.d(
        "Stage1",
        "Current word: ${wordDetail.word.word}, Correct index: $correctIndex"
    )

    // 启动反馈效果后延迟执行
    LaunchedEffect(showFeedback) {
        if (showFeedback) {
            delay(1000) // 显示反馈效果1秒
            if (isCorrect) {
                onCorrect()
            } else {
                onWrong()
            }
        }
    }

    // 中文解释展示区
    Column(
        modifier = Modifier.padding(start = 13.dp)
    ) {
        // 显示中文释义
        wordDetail.word.explanations?.forEach {
            Text(
                text = it,
                color = White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    Spacer(modifier = Modifier.height(20.dp))
    Text(
        text = "请从下列 4 个选项中选择正确单词",
        color = LightGrey,
        fontSize = 13.sp,
    )
    wordDetail.similarWords.forEachIndexed { index, option ->
        Spacer(modifier = Modifier.height(13.dp))

        Box(
            modifier = Modifier
                .clickable(enabled = !showFeedback) {
                    selectedOptionIndex = index
                    isCorrect = (index == correctIndex)
                    showFeedback = true
                }
                .height(80.dp)
                .fillMaxWidth()
                .background(Color.White.copy(0.1f), RoundedCornerShape(10.dp))
        ) {
            // 显示英文单词选项
            Box(
                modifier = Modifier.padding(18.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = option.word ?: "",
                    color = White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }

            // 反馈覆盖层
            if (showFeedback) {
                val feedbackColor = when {
                    index == correctIndex -> Color.Green.copy(alpha = 0.2f)
                    else -> Color.Red.copy(alpha = 0.2f)
                }
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(feedbackColor, RoundedCornerShape(10.dp))
                )
            }
        }
    }

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 93.dp)
    ) {
        Button(
            onClick = {
                // "看答案"按钮点击等同于选错
                selectedOptionIndex = -1  // 没有选中任何选项
                isCorrect = false         // 标记为错误
                showFeedback = true       // 显示反馈
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White.copy(alpha = 0.3f),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier.width(105.dp),
            enabled = !showFeedback
        ) {
            Text(
                text = "看答案", fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun ColumnScope.Stage2(
    wordDetail: ReviewWordInfo,
    onCorrect: () -> Unit,
    onWrong: () -> Unit
) {
    // 单词展示区
    Column(
        modifier = Modifier.padding(start = 13.dp)
    ) {
        Text(
            text = wordDetail.word.word ?: "",
            color = White,
            fontSize = 46.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // 读音
            Row(
                modifier = Modifier
                    .background(
                        color = Color.White.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(100.dp)
                    )
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "美",
                    color = LightGrey,
                    fontSize = 11.sp,
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = "播放读音",
                    tint = LightGrey,
                    modifier = Modifier.size(10.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            // 音标
            Text(
                text = wordDetail.word.phonetic ?: "",
                color = LightGrey,
                fontSize = 14.sp,
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "",
            color = LightGrey,
            fontSize = 13.sp,
        )
    }

    // 占位空间
    Spacer(modifier = Modifier.weight(1f))

    // 底部按钮组 - 认识和不认识
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 93.dp)
    ) {
        // 不认识按钮
        Button(
            onClick = {
                onWrong()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White.copy(alpha = 0.2f),
                contentColor = White
            ),
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier.width(105.dp)
        ) {
            Text(
                text = "不认识", fontSize = 16.sp
            )
        }

        // 认识按钮
        Button(
            onClick = {
                onCorrect()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White.copy(alpha = 0.3f),
                contentColor = White
            ),
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier.width(105.dp)
        ) {
            Text(
                text = "认识", fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun ColumnScope.Stage3(
    wordDetail: ReviewWordInfo,
    onCorrect: () -> Unit,
    onWrong: () -> Unit
) {
    // 中文解释展示区
    Column(
        modifier = Modifier.padding(start = 13.dp)
    ) {
        // 显示中文释义
        wordDetail.word.explanations?.forEach {
            val parts = it.split(".")
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "${parts[0]}.",
                    color = LightGrey,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                if (parts.size > 1) {
                    Text(
                        text = parts[1],
                        color = White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "看中文释义回想英文释义",
            color = LightGrey,
            fontSize = 13.sp,
        )
    }

    // 占位空间
    Spacer(modifier = Modifier.weight(1f))

    // 底部按钮组 - 认识和不认识
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 93.dp)
    ) {
        // 不认识按钮
        Button(
            onClick = {
                onWrong()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White.copy(alpha = 0.2f),
                contentColor = White
            ),
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier.width(105.dp)
        ) {
            Text(
                text = "不认识", fontSize = 16.sp
            )
        }

        // 认识按钮
        Button(
            onClick = {
                onCorrect()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White.copy(alpha = 0.3f),
                contentColor = White
            ),
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier.width(105.dp)
        ) {
            Text(
                text = "认识", fontSize = 16.sp
            )
        }
    }
}

