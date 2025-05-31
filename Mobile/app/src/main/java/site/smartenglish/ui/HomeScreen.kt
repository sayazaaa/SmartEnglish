package site.smartenglish.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import site.smartenglish.R
import site.smartenglish.ui.compose.BlurButton
import site.smartenglish.ui.compose.WordSearchItem
import site.smartenglish.ui.compose.WordSearchItemData
import site.smartenglish.ui.theme.LightOrange
import site.smartenglish.ui.theme.Orange
import site.smartenglish.ui.viewmodel.BackgroundImageViewmodel


@Composable
fun HomeScreen(
    viewmodel: BackgroundImageViewmodel = hiltViewModel()
) {
    val learnNum = 500
    val reviewNum = 100
    val titleWord = "English"

    val bitmap = viewmodel.backgroundBitmap.collectAsState().value


    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    val screenWidth = with(density) { windowInfo.containerSize.width.toDp() }
    val leftOffsetX = (screenWidth - 190.dp - 190.dp - 23.dp) / 2
    val rightOffsetX = leftOffsetX + 190.dp + 23.dp

    val focusManager = LocalFocusManager.current
    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }

    // 搜索模糊背景动画
    val blurRadiusAnim by animateFloatAsState(
        targetValue = if (isSearchActive) 100f else 0f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "blurAnim"
    )
    val backgroundAlphaAnim by animateFloatAsState(
        targetValue = if (isSearchActive) 0.5f else 0f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "alphaAnim"
    )

    val searchItemList = listOf<WordSearchItemData>(
        WordSearchItemData("Hello", "A greeting", isFav = false),
        WordSearchItemData(
            "World",
            "The earth, together with all of its countries and peoples",
            isFav = true
        ),
        WordSearchItemData("Compose", "A modern toolkit for building native UI", isFav = false),
        WordSearchItemData(
            "Android",
            "A mobile operating system developed by Google",
            isFav = true
        ),
        WordSearchItemData("Jetpack", "A suite of libraries for Android development", isFav = false)
    )


    val buttonContent: @Composable (text: String, num: Int) -> Unit = { t: String, n: Int ->
        Column(
            modifier = Modifier.padding(
                start = 24.dp,
            ),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = t,
                fontSize = 22.sp,
                lineHeight = with(density) { 48.dp.toSp() },
                color = Color(0xFFFFFEFD),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = n.toString(),
                fontSize = 16.sp,
                color = LightOrange,
                modifier = Modifier.offset(y = (-8).dp)
            )
        }
    }

    // 绘制背景图片
    Box {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        BlurButton(
            buttonWidth = 190.dp,
            buttonHeight = 77.dp,
            offset = Pair(leftOffsetX, 688.dp),
            bitmap = bitmap,
            blurRadius = 10.dp,
            onClick = { }
        ) { buttonContent("Learn", learnNum) }
        BlurButton(
            buttonWidth = 190.dp,
            buttonHeight = 77.dp,
            offset = Pair(rightOffsetX, 688.dp),
            bitmap = bitmap,
            blurRadius = 10.dp,
            onClick = { }
        ) { buttonContent("Review", reviewNum) }

        Text(
            text = titleWord,
            fontSize = 38.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 224.dp)
        )
        NavigationBar(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            containerColor = Color.Transparent,
            contentColor = Color.Transparent
        ) {
            // 首页图标
            val barItem: @Composable (
                imageVector: Any,
                contentDescription: String,
                onClick: () -> Unit,
            ) -> Unit = { image: Any, desc: String, onClick: () -> Unit ->
                NavigationBarItem(
                    modifier = Modifier.background(Color.Transparent),
                    icon = {
                        when (image) {
                            is ImageVector -> Icon(
                                imageVector = image,
                                contentDescription = desc,
                                modifier = Modifier.size(30.dp)
                            )
                            is Painter -> Icon(
                                painter = image,
                                contentDescription = desc,
                                modifier = Modifier.size(30.dp)
                            )
                            else -> throw IllegalArgumentException("Unsupported image type")
                        }
                    },
                    selected = false,
                    onClick = onClick,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        unselectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent,

                        )
                )
            }
            barItem(painterResource(R.drawable.dictionary), "外文阅读") { /* 处理点击 */ }
            barItem(painterResource(R.drawable.chart), "仪表盘") { /* 处理点击 */ }
            barItem(painterResource(R.drawable.edit_note), "单词听写") { /* 处理点击 */ }
        }
        // 搜索时模糊背景
        AnimatedVisibility(
            visible = isSearchActive,
            enter = fadeIn(tween(500)),
            exit = fadeOut(tween(500))
        ) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(blurRadiusAnim.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = backgroundAlphaAnim))
                    .clickable(
                        enabled = isSearchActive,
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = {
                            isSearchActive = false
                            searchQuery = ""
                            focusManager.clearFocus() // 清除焦点
                        }
                    )
            )
        }

        // 顶部图标 + 搜索框
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .statusBarsPadding()
                .padding(top = 5.dp, start = 5.dp, end = 29.dp)
        ) {
            AnimatedContent(
                targetState = isSearchActive,
                transitionSpec = {
                    (fadeIn(animationSpec = tween(200)) togetherWith
                            (fadeOut(animationSpec = tween(200))))
                },
                label = "HeaderButtonAnimation"
            ) { isActive ->
                if (isActive) {
                    // 返回按钮
                    IconButton(
                        onClick = {
                            isSearchActive = false
                            searchQuery = ""
                            focusManager.clearFocus() // 清除焦点
                        },
                        modifier = Modifier.size(51.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBackIos,
                            contentDescription = "Back",
                            modifier = Modifier.size(24.dp),
                            tint = Color.White.copy(alpha = 0.7f)
                        )
                    }
                } else {
                    // 个人头像按钮
                    IconButton(
                        modifier = Modifier
                            .size(51.dp)
                            .background(
                                Color.White.copy(alpha = 0.5f),
                                shape = CircleShape
                            ),
                        onClick = { }) {
                        AsyncImage(
                            model = "https://temp.im/50x50/?text=head",
                            contentDescription = "Profile",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(47.dp)
                                .clip(CircleShape)  // 确保图片为圆形
                        )
                    }
                }
            }
            Spacer(Modifier.width(17.dp))
            Box {
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .background(Color(0xFFFFFEFD), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            isSearchActive = it.isFocused
                        },
                    textStyle = LocalTextStyle.current.copy(
                        color = Color.DarkGray,
                        fontSize = 16.sp
                    ),
                    cursorBrush = SolidColor(Orange),
                    decorationBox = { innerTextField ->
                        // 搜索图标
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                modifier = Modifier.size(20.dp),
                                tint = Color.Black.copy(alpha = 0.3f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            // 显示输入框内容
                            innerTextField()
                        }
                    }
                )
            }
        }
        // 搜索结果内容
        AnimatedVisibility(
            visible = isSearchActive,
            enter = fadeIn(tween(500)),
            exit = fadeOut(tween(500))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 114.dp)
                    .background(Color.Transparent),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                searchItemList.forEach {
                    val (title, description, isFav) = it
                    WordSearchItem(title, description, {}, {}, isFav)
                }
            }
        }
    }

}