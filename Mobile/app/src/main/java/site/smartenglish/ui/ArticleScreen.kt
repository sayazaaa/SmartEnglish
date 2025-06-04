package site.smartenglish.ui

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import coil3.compose.AsyncImage
import kotlinx.coroutines.delay
import site.smartenglish.ui.theme.DarkGrey
import site.smartenglish.ui.theme.Grey
import site.smartenglish.ui.theme.LightGrey
import site.smartenglish.ui.theme.Orange
import site.smartenglish.ui.theme.White
import site.smartenglish.ui.viewmodel.ArticleViewmodel
import site.smartenglish.ui.viewmodel.SnackBarViewmodel


@Composable
fun ArticleScreen(
    navigateArticleDetail: (String) -> Unit = {},
    navigateBack: () -> Unit = {},
    navigateFavorite: () -> Unit = {},
    viewmodel: ArticleViewmodel = hiltViewModel(),
    snackBarViewmodel: SnackBarViewmodel = hiltViewModel(
        LocalActivity.current as ViewModelStoreOwner
    )
) {
    // 文章列表
    val articles = viewmodel.articleList.collectAsState().value ?: emptyList()
    val searchResults = viewmodel.searchResult.collectAsState().value ?: emptyList()

    // 搜索状态
    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var hasPerformedSearch by remember { mutableStateOf(false) }

    // 显示的列表 - 根据是否在搜索状态来决定显示原列表还是搜索结果
    val displayedArticles = if (isSearching && hasPerformedSearch) {
        searchResults
    } else {
        articles
    }

    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            ArticleScreenTopBar(
                onBackClick = navigateBack,
                isSearching = isSearching,
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onSearch = {
                    if (searchQuery.isNotEmpty()) {
                        viewmodel.searchArticle(searchQuery)
                        hasPerformedSearch = true
                    }
                },
                onToggleSearch = {
                    if (isSearching) {
                        // 退出搜索状态
                        isSearching = false
                        searchQuery = ""
                        hasPerformedSearch = false
                        viewmodel.clearSearchResult()
                    } else {
                        // 进入搜索状态
                        isSearching = true
                    }
                },
                navigateFavorite = navigateFavorite
            )
        }) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DarkGrey)
                    .verticalScroll(scrollState)
            ) {
                // 文章列表
                displayedArticles.forEach { article ->
                    article?.let { it ->
                        ArticleScreenItem(
                            onClick = {
                                if (it.id != null) {
                                    viewmodel.getArticleById(it.id)
                                    navigateArticleDetail(it.id)
                                } else {
                                    snackBarViewmodel.showSnackbar("文章ID为空")
                                }
                            },
                            title = it.title ?: "",
                            cover = it.cover,
                            date = it.date ?: "",
                            tag = it.tags?.map { it ?: "" }.let { it ?: emptyList() }
                        )
                    }
                }

                // 如果执行过搜索但没有结果
                if (isSearching && hasPerformedSearch && searchResults.isEmpty()) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "没有找到相关文章",
                        color = LightGrey,
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleScreenTopBar(
    navigateFavorite: () -> Unit,
    onBackClick: () -> Unit = {},
    isSearching: Boolean = false,
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    onSearch: () -> Unit = {},
    onToggleSearch: () -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }

    // 当切换到搜索状态时自动获取焦点
    LaunchedEffect(isSearching) {
        if (isSearching) {
            delay(100) // 短暂延迟确保动画开始后再获取焦点
            focusRequester.requestFocus()
        }
    }

    Column {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Grey
            ),
            title = {
                // 使用AnimatedContent在标题和搜索框之间平滑切换
                AnimatedContent(
                    targetState = isSearching,
                    transitionSpec = {
                        if (targetState) {
                            // 进入搜索模式
                            (slideInHorizontally { width -> width } + fadeIn()) togetherWith
                                    (slideOutHorizontally { width -> -width } + fadeOut())
                        } else {
                            // 退出搜索模式
                            (slideInHorizontally { width -> -width } + fadeIn()) togetherWith
                                    (slideOutHorizontally { width -> width } + fadeOut())
                        }.using(SizeTransform(clip = false))
                    },
                    label = "SearchBoxAnimation"
                ) { searching ->
                    if (searching) {
                        // 搜索模式下显示搜索输入框
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = onSearchQueryChange,
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(
                                color = White,
                                fontSize = 16.sp
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = { onSearch() }),
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
                                            onClick = { onSearchQueryChange("") },
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
                            text = "外文阅读",
                            color = White,
                            fontSize = 17.sp
                        )
                    }
                }
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "返回",
                        tint = White
                    )
                }
            },
            actions = {
                // 搜索/返回按钮动画
                AnimatedContent(
                    targetState = isSearching,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(150)) togetherWith
                                fadeOut(animationSpec = tween(150)))
                    },
                    label = "SearchIconAnimation"
                ) { searching ->
                    IconButton(onClick = onToggleSearch) {
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

                // 收藏夹图标的显示/隐藏动画
                AnimatedVisibility(
                    visible = !isSearching,
                    enter = fadeIn() + expandHorizontally(),
                    exit = fadeOut() + shrinkHorizontally()
                ) {
                    IconButton(
                        onClick = { navigateFavorite() },
                    ) {
                        Icon(
                            imageVector = Icons.Default.CollectionsBookmark,
                            contentDescription = "收藏夹",
                            tint = White
                        )
                    }
                }
            }
        )
    }
}
@Composable
fun ArticleScreenItem(
    onClick: () -> Unit = {},
    title: String = "文章标题",
    cover: String? = null,
    date: String = "",
    tag: List<String> = emptyList()
) {
    // 文章列表项的布局
    Spacer(Modifier.height(17.dp))
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        // 左侧内容区域
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // 标签
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.BookmarkBorder,
                    contentDescription = "标签图标",
                    tint = White,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = tag.joinToString(" · "),
                    color = White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            // 标题
            Text(
                text = title, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(50.dp))
        // 右侧：时间和图片
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = date, color = LightGrey, fontSize = 14.sp, fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            AsyncImage(
                model = cover ?: "https://via.placeholder.com/100",
                contentDescription = "封面图片",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(89.dp)
                    .background(Color.Black) // 深色背景
            )
        }
    }

}