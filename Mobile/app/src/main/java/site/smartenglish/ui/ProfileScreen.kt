package site.smartenglish.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import site.smartenglish.ui.theme.Grey
import site.smartenglish.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(

) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "个人信息",
                        color = White,
                        fontSize = 17.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {

                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回",
                            tint = White
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(Modifier.height(10.dp))
                // 头像区域
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(147.dp)
                        .background(Grey),
                    contentAlignment = Alignment.Center,

                    ) {
                    IconButton(
                        modifier = Modifier
                            .size(96.dp)
                            .background(
                                Color.White.copy(alpha = 0.1f),
                                shape = CircleShape
                            ),
                        onClick = { }) {
                        AsyncImage(
                            model = "https://temp.im/100/",
                            contentDescription = "Profile",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(87.dp)
                                .clip(CircleShape)
                        )
                    }
                    // 更换头像按钮
                    IconButton(
                        onClick = {
                            //TODO 打开相册或相机
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = (-10).dp)
                            .background(Grey, CircleShape)
                            .size(33.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "更换头像",
                            tint = White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                // 昵称区域
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                        .background(Grey)
                        .clickable { /*TODO*/ }
                        .padding(horizontal = 22.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "昵称",
                        color = White,
                        fontSize = 17.sp
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "用户名",
                            color = Color(0xFF878278),
                            fontSize = 16.sp
                        )
                        Spacer(Modifier.width(5.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = "编辑昵称",
                            tint = White,
                            modifier = Modifier.size(12.dp).offset(y = 1.dp)
                        )
                    }
                }
                // 手机号区域
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                        .background(Grey)
                        .padding(horizontal = 22.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "手机",
                        color = White,
                        fontSize = 17.sp
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "12343211234",
                            color = Color(0xFF878278),
                            fontSize = 16.sp
                        )
                        Spacer(Modifier.width(5.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = "编辑昵称",
                            tint = Color.Transparent,
                            modifier = Modifier.size(12.dp).offset(y = 1.dp)
                        )
                    }
                }
                // 反馈区域
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                        .background(Grey)
                        .clickable { /*TODO 打开反馈界面*/ }
                        .padding(horizontal = 22.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "用户反馈",
                        color = White,
                        fontSize = 17.sp
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Spacer(Modifier.width(5.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = "编辑昵称",
                            tint = White,
                            modifier = Modifier.size(12.dp).offset(y = 1.dp)
                        )
                    }
                }
            }
        }
    )
}