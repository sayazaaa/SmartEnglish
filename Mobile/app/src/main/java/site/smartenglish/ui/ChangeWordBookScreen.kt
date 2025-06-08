package site.smartenglish.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import kotlinx.coroutines.delay
import site.smartenglish.R
import site.smartenglish.remote.data.WordBook
import site.smartenglish.ui.theme.Grey
import site.smartenglish.ui.theme.LightGrey
import site.smartenglish.ui.theme.Orange
import site.smartenglish.ui.theme.White
import site.smartenglish.ui.viewmodel.WordBookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeWordBookScreen(
    navigateBack: () -> Unit,
    viewModel: WordBookViewModel = hiltViewModel()
) {
    val wordbooks = viewModel.wordbooks.collectAsState().value
    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var filteredWordbooks by remember { mutableStateOf<List<WordBook>>(emptyList()) }

    LaunchedEffect(searchQuery, wordbooks) {
        if (searchQuery.isNotEmpty()) {
            filteredWordbooks = wordbooks.filter { wordbook ->
                wordbook.name?.contains(searchQuery, ignoreCase = true) ?: false
            }
        } else {
            filteredWordbooks = wordbooks
        }
    }

    val displayedWordbooks = if (isSearching && searchQuery.isNotEmpty()) filteredWordbooks else wordbooks
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Grey
                ),
                title = {
                    AnimatedContent(
                        targetState = isSearching,
                        transitionSpec = {
                            if (targetState) {
                                (slideInHorizontally { width -> width } + fadeIn()) togetherWith
                                        (slideOutHorizontally { width -> -width } + fadeOut())
                            } else {
                                (slideInHorizontally { width -> -width } + fadeIn()) togetherWith
                                        (slideOutHorizontally { width -> width } + fadeOut())
                            }.using(SizeTransform(clip = false))
                        },
                        label = "SearchBoxAnimation"
                    ) { searching ->
                        if (searching) {
                            // 搜索栏
                            val focusRequester = remember { FocusRequester() }
                            LaunchedEffect(Unit) {
                                delay(100)
                                focusRequester.requestFocus()
                            }
                            BasicTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                singleLine = true,
                                textStyle = LocalTextStyle.current.copy(
                                    color = White,
                                    fontSize = 16.sp
                                ),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                cursorBrush = SolidColor(Orange),
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .height(38.dp)
                                    .background(
                                        Color(0xFFFFFEFD).copy(alpha = 0.15f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                    .focusRequester(focusRequester),
                                decorationBox = { innerTextField ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Start,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = "搜索",
                                            modifier = Modifier.size(20.dp),
                                            tint = White.copy(alpha = 0.7f)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Box(Modifier.weight(1f)) {
                                            if (searchQuery.isEmpty()) {
                                                Text(
                                                    "搜索词书",
                                                    color = LightGrey,
                                                    fontSize = 16.sp
                                                )
                                            }
                                            innerTextField()
                                        }
                                        if (searchQuery.isNotEmpty()) {
                                            IconButton(
                                                onClick = { searchQuery = "" },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(
                                                    Icons.Default.Clear,
                                                    contentDescription = "清除",
                                                    tint = White,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            )
                        } else {
                            Text(
                                text = "选择词书",
                                color = White,
                                fontSize = 17.sp
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBackIos,
                            contentDescription = "返回",
                            tint = White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                },
                actions = {
                    AnimatedContent(
                        targetState = isSearching,
                        transitionSpec = {
                            (fadeIn(animationSpec = tween(150)) togetherWith
                                    fadeOut(animationSpec = tween(150)))
                        },
                        label = "SearchIconAnimation"
                    ) { searching ->
                        IconButton(onClick = {
                            if (searching) {
                                searchQuery = ""
                            }
                            isSearching = !isSearching
                        }) {
                            if (searching) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.ArrowBackIos,
                                    contentDescription = "返回",
                                    tint = White,
                                    modifier = Modifier.size(16.dp)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "搜索",
                                    tint = White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
        ) {
            if (displayedWordbooks.isEmpty() && searchQuery.isNotEmpty()) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "没有找到相关词书",
                    color = LightGrey,
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                displayedWordbooks.forEach { wordbook ->
                    WordbookItem(
                        name = wordbook.name?:"",
                        cover = wordbook.cover,
                        wordCount = wordbook.wordcount?:0,
                        onClick = {
                            viewModel.selectWordBook(wordbook.id?:0)
                            navigateBack()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun WordbookItem(
    name: String,
    cover: String?,
    wordCount: Int,
    onClick: () -> Unit
) {
    Spacer(Modifier.height(16.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AsyncImage(
            model = cover,
            contentDescription = "词书封面",
            contentScale = ContentScale.Fit,
            error = painterResource(R.drawable.dictionary),
            modifier = Modifier
                .height(80.dp)
                .width(80.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = name,
                fontSize = 21.sp,
                color = White,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${wordCount}词",
                fontSize = 12.sp,
                color = Orange
            )
        }
    }
}