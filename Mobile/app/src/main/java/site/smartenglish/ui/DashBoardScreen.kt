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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Alarm
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import site.smartenglish.R
import site.smartenglish.ui.theme.DarkGrey
import site.smartenglish.ui.theme.Grey
import site.smartenglish.ui.theme.LightGrey
import site.smartenglish.ui.theme.Orange
import site.smartenglish.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoardScreen(
    onBackClick: () -> Unit = {}
) {
    val wordBookCover = "https://temp.im/50x100"
    val learnedWordNum = 800
    val totalWordNum = 2000

    val todayLearnedWord = 50 // 今日学习单词数
    val totalLearnedWord = 1000 // 累计学习单词数
    val todayStudyTime = 30 // 今日学习时长
    val totalStudyTime = 300 // 累计学习时长

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = DarkGrey
                ), title = {
                    Text(
                        text = "仪表盘", color = White, fontSize = 17.sp
                    )
                }, navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "返回",
                            tint = White
                        )
                    }
                })
        }) { innerPadding ->
        val scrollState = rememberScrollState()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp, start = 17.dp, end = 17.dp)
                    .verticalScroll(scrollState)
            ) {
                val titleRow: @Composable (titleText: String, buttonText: String, onClick: () -> Unit) -> Unit =
                    { t: String, b: String, o: () -> Unit ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = t,
                                modifier = Modifier.weight(1f),
                                color = White,
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Box(
                                modifier = Modifier
                                    .height(25.dp)
                                    .width(78.dp)
                                    .drawWithCache {
                                        onDrawBehind {
                                            drawRoundRect(
                                                Color(0xFF484540),
                                                cornerRadius = CornerRadius(12.dp.toPx())
                                            )
                                        }
                                    }
                                    .clickable { o() }) {
                                Text(
                                    text = b,
                                    color = Orange,
                                    fontSize = 13.sp,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }

                        }
                    }
                //正在学习块
                titleRow("正在学习", "换本词书") {}
                Spacer(modifier = Modifier.height(18.dp))
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth()
                        .background(Grey)
                        .padding(horizontal = 22.dp, vertical = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AsyncImage(
                            model = wordBookCover,
                            contentScale = ContentScale.Crop,
                            contentDescription = "词书封面",
                            modifier = Modifier
                                .width(95.dp)
                                .height(123.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .clickable {
                                    //TODO
                                })
                        val showMenu = remember { mutableStateOf(false) }
                        Box {
                            IconButton(
                                modifier = Modifier.offset(x = 10.dp), onClick = {
                                    showMenu.value = true
                                }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "更多操作",
                                    tint = LightGrey,
                                    modifier = Modifier
                                )
                            }

                            DropdownMenu(
                                expanded = showMenu.value,
                                onDismissRequest = { showMenu.value = false },
                                containerColor = Grey
                            ) {
                                DropdownMenuItem(
                                    text = { Text("查看词书", color = White) },
                                    onClick = {
                                        //TODO
                                        showMenu.value = false
                                    })
                            }
                        }
                    }
                    Spacer(Modifier.height(60.dp))
                    // 进度条
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(LightGrey)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(learnedWordNum.toFloat() / totalWordNum)
                                .height(6.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(Orange)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "已学习 $learnedWordNum",
                            color = LightGrey,
                            fontSize = 14.sp,
                        )
                        Text(
                            text = "总词汇 $totalWordNum",
                            color = LightGrey,
                            fontSize = 14.sp,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
                //我的数据块
                titleRow("我的数据", "已学单词") {
                    //TODO
                }
                Spacer(modifier = Modifier.height(18.dp))
                val dataItem: @Composable (icon: Any, iconColor: Color, title: String, value: String, unit: String) -> Unit =
                    { i, ic, t, v, u ->
                        Row(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            when (i) {
                                is ImageVector -> Icon(
                                    modifier = Modifier.size(20.dp),
                                    imageVector = i,
                                    contentDescription = null,
                                    tint = ic,
                                )

                                is Painter -> Icon(
                                    modifier = Modifier.size(20.dp),
                                    painter = i,
                                    contentDescription = null,
                                    tint = ic
                                )
                            }
                            Spacer(modifier = Modifier.width(9.dp))
                            Column(
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = t, fontSize = 13.sp, color = Color(0xFFCBCDCE)
                                )
                                Row(
                                    verticalAlignment = Alignment.Bottom,
                                    modifier = Modifier.padding(top = 11.dp)
                                ) {
                                    Text(
                                        text = v,
                                        fontSize = 26.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = White
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        text = u,
                                        fontSize = 12.sp,
                                        color = Color(0xFFCBCDCE).copy(alpha = 0.45f)
                                    )
                                }


                            }
                        }

                    }
                //card
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth()
                        .background(Grey)
                        .padding(vertical = 20.dp)
                ) {
                    // 第一行数据
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // 左上角项目
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 29.dp)
                        ) {
                            dataItem(
                                painterResource(R.drawable.signal),
                                Orange,
                                "今日学习",
                                todayLearnedWord.toString(),
                                "词"
                            )
                        }

                        // 右上角项目
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 29.dp)
                        ) {
                            dataItem(
                                painterResource(R.drawable.chart_line),
                                Color(0xFFBD5558),
                                "累计学习",
                                totalLearnedWord.toString(),
                                "词"
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(22.dp))
                    // 第二行数据
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // 左下角项目
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 29.dp)
                        ) {
                            dataItem(
                                Icons.Rounded.Alarm,
                                Orange,
                                "今日学习时长",
                                todayStudyTime.toString(),
                                "分钟"
                            )
                        }

                        // 右下角项目
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 29.dp)
                        ) {
                            dataItem(
                                Icons.Rounded.Alarm,
                                Color(0xFFBD5558),
                                "累计学习时长",
                                totalStudyTime.toString(),
                                "分钟"
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
                // 功能标签
                @Composable
                fun functionItem(text: String, onClick: () -> Unit) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Grey)
                            .clickable(onClick = onClick)
                            .padding(horizontal = 20.dp)
                            .height(61.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = text,
                            color = White,
                            fontSize = 16.sp
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                            contentDescription = "进入",
                            tint = LightGrey,
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                ) {
                    // 生词本标签
                    functionItem("生词本") {
                        // TODO: 处理点击事件
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                    // 听写标签
                    functionItem("听写") {
                        // TODO: 处理点击事件
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                    // 设置标签
                    functionItem("设置") {
                        // TODO: 处理点击事件
                    }
                }
            }
        }
    }
}