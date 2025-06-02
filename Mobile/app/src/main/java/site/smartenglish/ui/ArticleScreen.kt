package site.smartenglish.ui

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import site.smartenglish.ui.compose.tabIndicatorOffset
import site.smartenglish.ui.theme.DarkGrey
import site.smartenglish.ui.theme.Grey
import site.smartenglish.ui.theme.LightGrey
import site.smartenglish.ui.theme.Orange
import site.smartenglish.ui.theme.White

data class ArticleScreenItemData(
    val tag: List<String> = emptyList(),
    val title: String = "",
    val cover: String? = null,
    val date: String = ""
)


@Composable
fun ArticleScreen(
   navigateBack: () -> Unit = { /* TODO */ }
) {
    // 分类
    val tabs = listOf("热门", "科技", "文化")
    // 文章列表
    val articles = List(20) { index ->
        ArticleScreenItemData(
            title = "文章之标题！排版的秘密！ $index ".repeat(2),
            cover = "https://via.placeholder.com/150",
            date = "2023-10-${index + 1}"
        )
    }
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            ArticleScreenTopBar(
                onBackClick = navigateBack, tabs = tabs
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
                articles.forEach { article ->
                    ArticleScreenItem(
                        onClick = { /* TODO: 点击事件 */ },
                        tag = article.tag,
                        title = article.title,
                        cover = article.cover,
                        date = article.date
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleScreenTopBar(
    onBackClick: () -> Unit = { /* TODO */ }, tabs: List<String>
) {
    val selectedTabIndex = remember { mutableStateOf(0) }
    Column {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Grey
        ), title = {
            Text(
                text = "外文阅读", color = White, fontSize = 17.sp
            )
        }, navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "返回",
                    tint = White
                )
            }
        }, actions = {
            // 搜索
            IconButton(
                onClick = { /* TODO: 搜索功能 */ },
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "搜索",
                    tint = White
                )
            }
            // 收藏夹
            IconButton(
                onClick = { /* TODO: 收藏夹功能 */ },
            ) {
                Icon(
                    imageVector = Icons.Default.CollectionsBookmark,
                    contentDescription = "收藏夹",
                    tint = White
                )
            }

        })

        TabRow(
            selectedTabIndex = selectedTabIndex.value,
            containerColor = Grey,
            contentColor = Grey,
            indicator = { tabPositions ->
                if (selectedTabIndex.value < tabPositions.size) {
                    val tabPosition = tabPositions[selectedTabIndex.value]
                    Box(
                        Modifier
                            .tabIndicatorOffset(tabPosition)
                            .background(color = Orange)
                            .height(4.dp)

                    )
                }
            },
            divider = {}) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex.value == index,
                    onClick = { selectedTabIndex.value = index },
                    text = {
                        Text(
                            title, color = White, fontSize = 16.sp
                        )
                    })
            }
        }
    }
}

@Composable
fun ArticleScreenItem(
    onClick: () -> Unit = { /* TODO */ },
    tag: List<String>,
    title: String = "文章标题",
    cover: String? = null,
    date: String = "2023-10-01",
) {
    // 文章列表项的布局
    Spacer(Modifier.height(17.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clickable(onClick = onClick),
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
                modifier = Modifier
                    .size(89.dp)
                    .background(Color.Black) // 深色背景
            )
        }
    }

}