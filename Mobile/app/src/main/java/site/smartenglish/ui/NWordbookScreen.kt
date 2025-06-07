package site.smartenglish.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
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
import site.smartenglish.ui.viewmodel.NWordBookViewmodel
import site.smartenglish.ui.viewmodel.UploadImageViewmodel

@Composable
fun NWordBookScreen(
    viewmodel: NWordBookViewmodel = hiltViewModel(),
    imageViewmodel: UploadImageViewmodel = hiltViewModel(),
    navigateNWordBookDetail: (Int, String) -> Unit = { _, _ -> },
    navigateBack: () -> Unit = { }
) {
    val favList = viewmodel.nWordBookList.collectAsState().value
    val favDetail = viewmodel.nWordBookDetail.collectAsState().value

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
        viewmodel.getNWordBookList()
        viewmodel.getNWordBookDetail()
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
                            viewmodel.createNWordBookSet(favName, url).join()
                            // 创建完成后刷新收藏夹列表
                            viewmodel.getNWordBookList()
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
                    .background(Grey)
                    .padding(paddingValues)
                    .padding(start = 16.dp)
                    .verticalScroll(scrollState)
                    ,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                favList?.filterNotNull()?.forEachIndexed{index, set ->
                    if (index == 0) {
                        Box(modifier = Modifier.height(22.dp).fillMaxWidth()) {
                            Text(
                                "默认生词本",
                                fontSize = 14.sp,
                                color = LightGrey,
                                modifier = Modifier.padding(start = 5.dp)
                            )
                        }
                    }
                    NWordBookScreenItem(
                        title = set.name ?: "未知",
                        cover = set.cover?:"",
                        num = favDetail[set.id ?: 0]?.size ?: 0,
                        onClick = {
                            navigateNWordBookDetail(set.id ?: 0, set.name ?: "未知")
                        },
                    )
                    if (index == 0) {
                        Box(modifier = Modifier.height(22.dp).fillMaxWidth()) {
                            Text(
                                "自建生词本",
                                fontSize = 14.sp,
                                color = LightGrey,
                                modifier = Modifier.padding(start = 5.dp)
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(112.dp)
                        .padding(top = 9.dp, bottom = 9.dp)
                        .clickable {
                            showDialog = true
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    AsyncImage(
                        model = null,
                        contentScale = ContentScale.Fit,
                        error = painterResource( R.drawable.new_window),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(White),
                        modifier = Modifier.fillMaxHeight()
                    )
                    Spacer(Modifier.width(10.dp))
                        Text(
                            text = "新建生词本",
                            fontSize = 18.sp,
                            color = White,
                            modifier = Modifier.weight(1f)
                        )
                }
            }
        },
    )
}

@Composable
fun NWordBookScreenItem(
    title: String,
    cover:String,
    num:Int,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(112.dp)
            .padding(top = 9.dp, bottom = 9.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        AsyncImage(
            model = cover,
            contentScale = ContentScale.Fit,
            error = painterResource(R.drawable.words),
            contentDescription = null,
            modifier = Modifier.fillMaxHeight()
        )
        Spacer(Modifier.width(20.dp))
        Column {
            Spacer(Modifier.height(10.dp))
            Text(
                text = title,
                fontSize = 18.sp,
                color = White,
                modifier = Modifier.weight(1f)
            )
            Text(
                text ="${num}词",
                fontSize = 14.sp,
                color = LightGrey,
                modifier = Modifier.weight(1f)
            )
        }

    }
}




