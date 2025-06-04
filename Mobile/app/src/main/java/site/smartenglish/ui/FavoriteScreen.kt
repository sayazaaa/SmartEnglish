package site.smartenglish.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import site.smartenglish.R
import site.smartenglish.ui.compose.CenterAlignedBackArrowTopAppBar
import site.smartenglish.ui.theme.Grey
import site.smartenglish.ui.theme.LightGrey
import site.smartenglish.ui.theme.White
import site.smartenglish.ui.viewmodel.FavoriteViewmodel
import site.smartenglish.ui.viewmodel.UploadImageViewmodel

data class FavoriteListItemData(
    val id: String,
    val title: String,
    val date: String = "0000-00-00",
    val cover: String? = null,
)

@Composable
fun FavoriteScreen(
    viewmodel: FavoriteViewmodel = hiltViewModel(),
    imageViewmodel: UploadImageViewmodel = hiltViewModel(),
    navigateArticleDetail: (String) -> Unit,
    navigateFavoriteDetail: (Int, String) -> Unit = { _, _ -> },
    navigateBack: () -> Unit = { }
) {
    val favList = viewmodel.favoritesList.collectAsState().value
    val favDetail = viewmodel.favoritesDetail.collectAsState().value

    val scope = rememberCoroutineScope()


    // 创建收藏夹对话框状态
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

    LaunchedEffect(Unit) {
        viewmodel.getFavoriteList()
        viewmodel.getFavoriteDetail()
    }

    // 创建收藏夹对话框
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    "新建收藏夹",
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
                            viewmodel.createFavoriteSet(favName, url).join()
                            // 创建完成后刷新收藏夹列表
                            viewmodel.getFavoriteList()
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

    Scaffold(
        topBar = {
            CenterAlignedBackArrowTopAppBar(
                title = "收藏",
                onBackClick = navigateBack,
                actions = {
                    // 添加收藏
                    IconButton(
                        onClick = { showDialog = true },
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.new_window),
                            contentDescription = "添加收藏夹",
                            tint = White
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(start = 16.dp)
                    .verticalScroll(scrollState)
            ) {
                favList?.filterNotNull()?.forEach { set ->
                    FavoriteScreenItem(
                        title = set.name ?: "未知收藏夹",
                        articleList = favDetail[set.id ?: 0] ?: emptyList(),
                        onClick = {
                            navigateFavoriteDetail(set.id ?: 0, set.name ?: "未知收藏夹")
                        },
                        onArticleClick = {
                            navigateArticleDetail(it)
                        },
                        onDeleteSet = {
                            viewmodel.deleteFavoriteSet(set.id ?: 0)
                            viewmodel.getFavoriteList()
                        },
                        onRemoveArticle = { articleId ->
                            viewmodel.removeFromFavorite(articleId, set.id ?: 0)
                            viewmodel.getFavoriteDetail()
                        }

                    )
                }
            }
        },
    )
}

@Composable
fun FavoriteScreenItem(
    title: String,
    articleList: List<FavoriteListItemData> = emptyList(),
    onClick: () -> Unit = {},
    onArticleClick: (String) -> Unit = {},
    onDeleteSet: () -> Unit = {},
    onRemoveArticle: (String) -> Unit = {},
) {
    val articleNum = articleList.size

    // 控制对话框显示
    var showDeleteSetDialog by remember { mutableStateOf(false) }
    var showRemoveArticleDialog by remember { mutableStateOf(false) }
    var articleToRemove by remember { mutableStateOf("") }

    Spacer(Modifier.height(16.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = { showDeleteSetDialog = true }
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = White
        )
        Row(
            modifier = Modifier
                .height(24.dp)
                .padding(end = 16.dp)
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "共${articleNum}篇",
                modifier = Modifier,
                fontSize = 14.sp,
                color = White
            )
            Icon(
                Icons.AutoMirrored.Default.ArrowForwardIos,
                contentDescription = "Arrow Forward",
                modifier = Modifier.size(8.dp),
                tint = White
            )
        }
    }

    // 收藏夹删除确认对话框
    if (showDeleteSetDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteSetDialog = false },
            title = { Text("删除收藏夹", color = White) },
            containerColor = Grey,
            text = { Text("确定要删除「${title}」收藏夹吗？此操作不可撤销。", color = White) },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteSet()
                        showDeleteSetDialog = false
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDeleteSetDialog = false },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("取消")
                }
            }
        )
    }

    Spacer(Modifier.height(8.dp))
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        articleList.forEach {
            Box(
                modifier = Modifier
                    .height(250.dp)
                    .width(350.dp)
                    .combinedClickable(
                        onClick = { onArticleClick(it.id) },
                        onLongClick = {
                            articleToRemove = it.id
                            showRemoveArticleDialog = true
                        }
                    )
                    .clip(RoundedCornerShape(12.dp))
            ) {
                AsyncImage(
                    model = it.cover ?: "",
                    contentDescription = "文章封面",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0xCC212532) // 添加透明度
                                ),
                                startY = 0f,
                                endY = Float.POSITIVE_INFINITY
                            )
                        )
                )

                // 文本放在最上层
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = it.title,
                        modifier = Modifier.padding(horizontal = 20.dp),
                        fontSize = 18.sp,
                        maxLines = 2,
                        color = White.copy(alpha = 0.9f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = it.date,
                        modifier = Modifier.padding(start = 20.dp, bottom = 13.dp),
                        fontSize = 14.sp,
                        color = White.copy(alpha = 0.6f)
                    )
                }
            }
            Spacer(Modifier.width(16.dp))
        }
    }

    // 文章移出收藏夹确认对话框
    if (showRemoveArticleDialog) {
        AlertDialog(
            onDismissRequest = { showRemoveArticleDialog = false },
            title = { Text("移出收藏夹", color = White) },
            containerColor = Grey,
            text = { Text("确定要将此文章从「${title}」收藏夹中移除吗？", color = White) },
            confirmButton = {
                Button(
                    onClick = {
                        onRemoveArticle(articleToRemove)
                        showRemoveArticleDialog = false
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showRemoveArticleDialog = false },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("取消")
                }
            }
        )
    }
}

