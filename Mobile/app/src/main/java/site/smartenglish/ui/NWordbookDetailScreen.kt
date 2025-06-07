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
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import site.smartenglish.ui.theme.Grey
import site.smartenglish.ui.theme.LightGrey
import site.smartenglish.ui.theme.Orange
import site.smartenglish.ui.theme.White
import site.smartenglish.ui.viewmodel.NWordBookViewmodel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NWordBookDetailScreen(
    setId: Int,
    title: String,
    navigateBack: () -> Unit,
    viewmodel: NWordBookViewmodel = hiltViewModel()
) {
    // 状态变量
    val words = viewmodel.nWordBookDetail.collectAsState().value[setId] ?: emptyList()
    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var filteredWords by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(Unit) {
        viewmodel.getNWordBookDetail()
    }

    LaunchedEffect(searchQuery, words) {
        if (searchQuery.isNotEmpty()) {
            filteredWords = words.filter {
                it.contains(searchQuery, ignoreCase = true)
            }
        } else {
            filteredWords = words
        }
    }

    val displayedWords =
        if (isSearching && searchQuery.isNotEmpty()) filteredWords else words
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
                            // 搜索栏 - 类似ArticleScreen
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
                                                    "输入搜索关键词",
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
                                text = title,
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
            if (displayedWords.isEmpty() && searchQuery.isNotEmpty()) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "没有找到单词",
                    color = LightGrey,
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                Text("${displayedWords.size}词", fontSize = 24.sp, color = White, modifier = Modifier.padding(vertical = 16.dp), fontWeight = FontWeight.Bold)
                HorizontalDivider(color = LightGrey)
                displayedWords.forEach {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(it, fontSize = 20.sp, color = White, modifier = Modifier.padding(vertical = 16.dp), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
