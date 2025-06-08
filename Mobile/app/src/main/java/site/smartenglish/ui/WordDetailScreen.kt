package site.smartenglish.ui

import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import site.smartenglish.R
import site.smartenglish.ui.compose.FavBottomSheets
import site.smartenglish.ui.compose.FavBottomSheetsItemData
import site.smartenglish.ui.theme.Grey
import site.smartenglish.ui.theme.LightGrey
import site.smartenglish.ui.theme.White
import site.smartenglish.ui.viewmodel.AudioPlayerViewModel
import site.smartenglish.ui.viewmodel.BackgroundImageViewmodel
import site.smartenglish.ui.viewmodel.NWordBookViewmodel
import site.smartenglish.ui.viewmodel.SnackBarViewmodel
import site.smartenglish.ui.viewmodel.UploadImageViewmodel
import site.smartenglish.ui.viewmodel.WordDetailViewmodel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordDetailScreen(
    navigateBack: () -> Unit = {},
    word: String = "",
    viewmodel: WordDetailViewmodel = hiltViewModel(),
    bgViewmodel: BackgroundImageViewmodel = hiltViewModel(),
    nWordBookViewmodel: NWordBookViewmodel = hiltViewModel(),
    imageViewmodel: UploadImageViewmodel = hiltViewModel(),
    audioPlayerViewModel: AudioPlayerViewModel = hiltViewModel(),
    snackBarViewmodel: SnackBarViewmodel = hiltViewModel(LocalActivity.current as ViewModelStoreOwner)
) {
    val bitmap = bgViewmodel.backgroundBitmap.collectAsState().value
    val wordDetail = viewmodel.wordDetail.collectAsState().value
    val snackBar = viewmodel.snackBar.collectAsState().value

    val scope = rememberCoroutineScope()


    // 创建收藏夹对话框状态
    val isFav = nWordBookViewmodel.isNWordBook.collectAsState().value
    val favList = nWordBookViewmodel.nWordBookList.collectAsState().value
    var isFavShow by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var favName by remember { mutableStateOf("") }
    val url =
        (imageViewmodel.uploadState.collectAsState().value as? UploadImageViewmodel.UploadState.Success)?.imageUrl
            ?: ""

    // 图片选择器
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            imageViewmodel.uploadImage(uri)
        }
    }

    // 创建收藏夹对话框
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    "新建生词本",
                    color = White,
                    fontWeight = FontWeight.W400
                )
            },
            containerColor = Grey,
            text = {
                Column {
                    OutlinedTextField(
                        value = favName,
                        onValueChange = { favName = it },
                        label = { Text("收藏夹名称", color = LightGrey) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = White,
                            unfocusedTextColor = White
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            // 调用系统图片选择器
                            imagePicker.launch("image/*")
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("选择封面图片")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    when (imageViewmodel.uploadState.collectAsState().value) {
                        is UploadImageViewmodel.UploadState.Progress -> {
                            Text("上传中...", color = LightGrey)
                        }

                        is UploadImageViewmodel.UploadState.Success -> {
                            Text("上传成功", color = White)
                            AsyncImage(
                                model = (imageViewmodel.uploadState.collectAsState().value as UploadImageViewmodel.UploadState.Success).imageUrl,
                                contentDescription = "封面图片",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }

                        is UploadImageViewmodel.UploadState.Error -> {
                            Text("上传失败", color = LightGrey)
                        }

                        else -> {
                            // Idle 状态不显示任何内容
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            // 先创建收藏夹并等待完成
                            nWordBookViewmodel.createNWordBookSet(favName, url).join()
                            // 创建完成后刷新收藏夹列表
                            nWordBookViewmodel.getNWordBookList()
                            // 重置状态
                            favName = ""
                            imageViewmodel.resetUploadState()
                            showDialog = false
                        }
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("创建")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("取消")
                }
            }
        )
    }


    LaunchedEffect(Unit) {
        nWordBookViewmodel.getIsNWordBook(word)
        viewmodel.getWordDetail(word)
    }

    LaunchedEffect(snackBar) {
        if (snackBar.isNotEmpty()) {
            snackBarViewmodel.showSnackbar(snackBar)
            viewmodel.clearSnackBar() // 清除 snackbar 内容
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            audioPlayerViewModel.stopAudio()
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
                    }
                }, actions = {
                    IconButton(
                        onClick = {
                            isFavShow = true
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
        if (isFavShow) {
            FavBottomSheets(
                title = "将单词收藏至",
                onDismiss = { isFavShow = false },
                onCreateFav = {
                    // 点击"新建收藏夹"时显示对话框
                    showDialog = true
                },
                error = painterResource(R.drawable.news),
                items = favList?.filterNotNull()?.mapNotNull { set ->
                    // 过滤掉 null 项
                    if (set.id == null || set.name == null) return@mapNotNull null
                    FavBottomSheetsItemData(
                        setId = set.id,
                        title = set.name,
                        cover = set.cover ?: "",
                        onAddToFav = {
                            // 添加到收藏夹
                            nWordBookViewmodel.addToNWordBook(wordDetail.word ?: "", set.id)
                        },
                        onRemoveFromFav = {
                            // 从收藏夹中移除
                            nWordBookViewmodel.removeFromNWordBook(wordDetail.word ?: "", set.id)
                        },
                        isFav = isFav[set.id] ?: false
                    )
                } ?: emptyList()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 17.dp, top = 37.dp, end = 17.dp)
                .padding(padding)
                .background(Color.Transparent)
        ) {
            // detail screen
            // 单词展示区
            Column(
                modifier = Modifier.padding(start = 13.dp)
            ) {
                Text(
                    text = wordDetail.word ?: "",
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
                            .clickable {
                                audioPlayerViewModel.playAudio(wordDetail.pronunciation ?: "")
                            }
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
                        text = wordDetail.phonetic ?: "",
                        color = LightGrey,
                        fontSize = 14.sp,
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                wordDetail.explanations?.forEach {
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
                !wordDetail.examples.isNullOrEmpty()
            val hasSynonyms =
                !(wordDetail.synonyms?.a.isNullOrEmpty() &&
                        wordDetail.synonyms?.v.isNullOrEmpty() &&
                        wordDetail.synonyms?.n.isNullOrEmpty())
            val hasAntonyms =
                !(wordDetail.antonyms?.a.isNullOrEmpty() &&
                        wordDetail.antonyms?.v.isNullOrEmpty() &&
                        wordDetail.antonyms?.n.isNullOrEmpty())

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
                                            // 为每个例句添加点击播放功能
                                            wordDetail.examples?.forEach { example ->
                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .clickable {
                                                            example?.audio.let { audioUrl ->
                                                                audioPlayerViewModel.playAudio(audioUrl)
                                                            }
                                                        }
                                                        .padding(8.dp)
                                                ) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text(
                                                            text = example?.english ?: "",
                                                            color = White,
                                                            fontSize = 15.sp,
                                                            modifier = Modifier.weight(1f)
                                                        )

                                                        // 添加播放图标
                                                        Icon(
                                                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                                            contentDescription = "播放例句",
                                                            tint = LightGrey,
                                                            modifier = Modifier.size(16.dp)
                                                        )
                                                    }

                                                    Text(
                                                        text = example?.chinese ?: "",
                                                        color = LightGrey,
                                                        fontSize = 12.sp,
                                                        modifier = Modifier.padding(bottom = 16.dp)
                                                    )
                                                }
                                            }

                                            // 底部添加提示信息
                                            Text(
                                                text = "点击例句可播放音频",
                                                color = LightGrey,
                                                fontSize = 12.sp,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(top = 8.dp),
                                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                            )
                                        }
                                    }

                                    "同义词" -> { // 同义词
                                        if (wordDetail.synonyms?.a?.isNotEmpty() == true) {
                                            Text(
                                                text = "a.",
                                                color = White,
                                                fontSize = 16.sp,
                                                modifier = Modifier.padding(vertical = 2.dp),
                                                fontWeight = FontWeight.Bold
                                            )
                                            wordDetail.synonyms?.a?.forEach { synonym ->
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

                                        if (wordDetail.synonyms?.v?.isNotEmpty() == true) {
                                            Text(
                                                text = "v.",
                                                color = White,
                                                fontSize = 16.sp,
                                                modifier = Modifier.padding(vertical = 2.dp),
                                                fontWeight = FontWeight.Bold
                                            )
                                            wordDetail.synonyms?.v?.forEach { synonym ->
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

                                        if (wordDetail.synonyms?.n?.isNotEmpty() == true) {
                                            Text(
                                                text = "n.",
                                                color = White,
                                                fontSize = 16.sp,
                                                modifier = Modifier.padding(vertical = 2.dp),
                                                fontWeight = FontWeight.Bold
                                            )
                                            wordDetail.synonyms?.n?.forEach { synonym ->
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
                                        if (wordDetail.antonyms?.a?.isNotEmpty() == true) {
                                            Text(
                                                text = "a.",
                                                color = White,
                                                fontSize = 16.sp,
                                                modifier = Modifier.padding(vertical = 2.dp),
                                                fontWeight = FontWeight.Bold
                                            )
                                            wordDetail.antonyms?.a?.forEach { antonym ->
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

                                        if (wordDetail.antonyms?.v?.isNotEmpty() == true) {
                                            Text(
                                                text = "v.",
                                                color = White,
                                                fontSize = 16.sp,
                                                modifier = Modifier.padding(vertical = 2.dp),
                                                fontWeight = FontWeight.Bold
                                            )
                                            wordDetail.antonyms?.v?.forEach { antonym ->
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

                                        if (wordDetail.antonyms?.n?.isNotEmpty() == true) {
                                            Text(
                                                text = "n.",
                                                color = White,
                                                fontSize = 16.sp,
                                                modifier = Modifier.padding(vertical = 2.dp),
                                                fontWeight = FontWeight.Bold
                                            )
                                            wordDetail.antonyms?.n?.forEach { antonym ->
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
                }
    }
}