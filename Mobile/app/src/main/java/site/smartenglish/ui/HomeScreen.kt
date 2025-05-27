package site.smartenglish.ui

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import site.smartenglish.ui.compose.BlurButton
import site.smartenglish.ui.theme.LightOrange
import site.smartenglish.ui.viewmodel.HomeViewmodel

@Composable
fun HomeScreen(
    viewmodel: HomeViewmodel = hiltViewModel()
) {
    val learnNum = 500
    val reviewNum = 100
    val imageUrl = "https://images.pexels.com/photos/32241306/pexels-photo-32241306.jpeg"
    val titleWord = "English"

    val bitmap = viewmodel.backgroundBitmap.collectAsState().value


    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    val screenWidth = with(density) { windowInfo.containerSize.width.toDp() }
    val leftOffsetX = (screenWidth - 190.dp - 190.dp - 23.dp) / 2
    val rightOffsetX = leftOffsetX + 190.dp + 23.dp

    LaunchedEffect(Unit) {
        viewmodel.loadBackgroundImage(imageUrl)
    }

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
        // 顶部图标 + 搜索框
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .statusBarsPadding()
                .padding(top = 5.dp, start = 5.dp, end = 29.dp)
        ) {
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
            Spacer(Modifier.width(17.dp))
            Box {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .clickable { /* 这里添加导航到搜索页面的逻辑 */ },
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White.copy(alpha = 0.9f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "搜索",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
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
                imageVector: ImageVector,
                contentDescription: String,
                onClick: () -> Unit,
            ) -> Unit = { image: ImageVector, desc: String, onClick: () -> Unit ->
                NavigationBarItem(
                    modifier = Modifier.background(Color.Transparent),
                    icon = {
                        Icon(
                            image,
                            contentDescription = desc,
                            modifier = Modifier.size(30.dp)
                        )
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
            barItem(Icons.Default.Check, "首页") { /* 处理点击 */ }
            barItem(Icons.Default.Image, "练习") { /* 处理点击 */ }
            barItem(Icons.Default.Mic, "我的") { /* 处理点击 */ }
        }
    }

}