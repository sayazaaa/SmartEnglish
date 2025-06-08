package site.smartenglish.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import site.smartenglish.R
import site.smartenglish.ui.compose.CenterAlignedBackArrowTopAppBar
import site.smartenglish.ui.compose.FavBottomSheets
import site.smartenglish.ui.compose.FavBottomSheetsItemData
import site.smartenglish.ui.compose.HtmlRender
import site.smartenglish.ui.compose.ScrollBar
import site.smartenglish.ui.theme.Grey
import site.smartenglish.ui.theme.LightGrey
import site.smartenglish.ui.theme.Orange
import site.smartenglish.ui.theme.White
import site.smartenglish.ui.viewmodel.ArticleDetailViewmodel
import site.smartenglish.ui.viewmodel.FavoriteViewmodel
import site.smartenglish.ui.viewmodel.UploadImageViewmodel
import site.smartenglish.ui.viewmodel.UsingViewModel

@Composable
fun ArticleDetailScreen(
    articleId: String,
    navigateBack: () -> Unit = { },
    viewmodel: ArticleDetailViewmodel = hiltViewModel(),
    favoriteViewmodel: FavoriteViewmodel = hiltViewModel(),
    imageViewmodel: UploadImageViewmodel = hiltViewModel(),
    usingViewModel: UsingViewModel = hiltViewModel()
) {

    val articleDetail = viewmodel.articleDetail.collectAsState().value
    val isAnyFav = favoriteViewmodel.isAnyFavorite.collectAsState().value
    val isFav = favoriteViewmodel.isFavorite.collectAsState().value
    val date = articleDetail.date ?: ""
    val html = (articleDetail.content ?: "").trim()

    val favList = favoriteViewmodel.favoritesList.collectAsState().value

    // 显示创建收藏夹的对话框
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

    // 确认删除收藏夹对话框状态
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedSetIdToDelete by remember { mutableStateOf<Int?>(null) }

    // 在组件进入时开始计时
    LaunchedEffect(Unit) {
        usingViewModel.startTracking()
    }

    // 在组件退出时发送使用时间
    DisposableEffect(Unit) {
        onDispose {
            usingViewModel.sendUsageTime("read")
        }
    }

    // 当应用进入后台时暂停计时，回到前台时继续计时
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    usingViewModel.pauseTracking()
                }
                Lifecycle.Event.ON_RESUME -> {
                    usingViewModel.startTracking()
                }
                else -> {} // 其他生命周期事件不处理
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }



    LaunchedEffect(articleId) {
        // 加载文章详情
        viewmodel.getArticleById(articleId)
        // 获取收藏夹列表
        favoriteViewmodel.getIsFavorite(articleId)
    }



    Scaffold(
        topBar = {
            CenterAlignedBackArrowTopAppBar(
                title = "", onBackClick = navigateBack, actions = {
                    IconButton(
                        onClick = {
                            // 打开底部弹窗
                            isFavShow = true
                        }, modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            painter = painterResource(
                                if (isAnyFav) R.drawable.kid_star_fill else R.drawable.kid_star
                            ),
                            contentDescription = "收藏",
                            tint = if (isAnyFav) Orange else White,
                        )
                    }
                })
        },
    ) { inn ->

        // 获取父容器高度
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(inn)
        ) {
            val density = LocalDensity.current
            val parentHeight = maxHeight
            val scrollState = rememberScrollState()
            val scope = rememberCoroutineScope() // 添加协程作用域

            var contentHeight by remember { mutableStateOf(0) }
            val viewportPx = with(density) { parentHeight.roundToPx() }

            if (isFavShow) {
                FavBottomSheets(
                    title = "将文章收藏至",
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
                                favoriteViewmodel.addToFavorite(articleId, set.id)
                            },
                            onRemoveFromFav = {
                                // 从收藏夹中移除
                                favoriteViewmodel.removeFromFavorite(articleId, set.id)
                            },
                            onLongPress = {
                                // 长按时显示确认删除对话框
                                selectedSetIdToDelete = set.id
                                showDeleteDialog = true
                            },
                            isFav = isFav[set.id] ?: false
                        )
                    } ?: emptyList()
                )
            }


            // 删除确认对话框
            if (showDeleteDialog && selectedSetIdToDelete != null) {
                AlertDialog(
                    onDismissRequest = {
                        showDeleteDialog = false
                        selectedSetIdToDelete = null
                    },
                    title = {
                        Text(
                            "删除收藏夹",
                            color = White,
                            fontWeight = FontWeight.W400
                        )
                    },
                    containerColor = Grey,
                    text = {
                        Text(
                            "确定要删除这个收藏夹吗？此操作不可撤销。",
                            color = White
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                selectedSetIdToDelete?.let { setId ->
                                    scope.launch {
                                        favoriteViewmodel.deleteFavoriteSet(setId).join()
                                        // 删除成功后刷新收藏状态
                                        favoriteViewmodel.getIsFavorite(articleId)
                                    }
                                }
                                showDeleteDialog = false
                                selectedSetIdToDelete = null
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("确定")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                showDeleteDialog = false
                                selectedSetIdToDelete = null
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("取消")
                        }
                    }
                )
            }



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
                                    favoriteViewmodel.createFavoriteSet(favName, url).join()
                                    // 创建完成后再检查收藏状态
                                    favoriteViewmodel.getIsFavorite(articleId)
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
                            onClick = { showDialog = false }, shape = RoundedCornerShape(8.dp)

                        ) {
                            Text("取消")
                        }
                    }
                )
            }



            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 27.dp, end = 27.dp, top = 27.dp)
                    .verticalScroll(scrollState)
                    .onSizeChanged { size ->
                        contentHeight = size.height
                    }) {
                // 内容部分
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(17.dp)
                            .background(Orange)
                    )
                    Spacer(Modifier.width(11.dp))
                    Text(
                        text = date,
                        color = White,
                        modifier = Modifier.padding(top = 2.dp),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.times))
                    )
                }
                Spacer(Modifier.height(20.dp))
                HtmlRender(html = html)
            }

            // 滚动条
            ScrollBar(scrollPosition = {
                if (scrollState.maxValue > 0) scrollState.value.toFloat() / scrollState.maxValue
                else 0f
            }, scrollHeight = {
                if (contentHeight > viewportPx) viewportPx.toFloat() / contentHeight
                else 0f
            }, onScrollPositionChange = { newPosition ->
                val newScrollValue = (newPosition * scrollState.maxValue).toInt()
                scope.launch {
                    scrollState.scrollTo(newScrollValue)
                }
            })
        }
    }
}


