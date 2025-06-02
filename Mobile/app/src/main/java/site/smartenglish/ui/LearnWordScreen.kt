package site.smartenglish.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*

@Composable
fun LearnWordScreen() {
    val mainColor = Color(0xFF006400)
    val textColor = Color.White
    val darkTextColor = Color.LightGray
    val hintColor = Color(0xFFE1E3E5)
    val rightIndex = 1
    val soundType = "美"
    val words= listOf(
        "revert",
        "exile",
        "escort",
        "resort"
    )
    val wordTypes = listOf(
        "vi.",
        "vt.",
        "vt.",
        "n."
    )
    val wordMeanings = listOf(
        "111",
        "222",
        "333",
        "444"
    )
    var selected by remember { mutableStateOf<Boolean>(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(mainColor)
            .padding(16.dp)
    ) {
        // 页面角落辅助功能组件
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "返回",
                tint = textColor,
                modifier = Modifier.size(24.dp)
            )
            Row {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "收藏",
                    tint = textColor,
                    modifier = Modifier.size(24.dp)
                )
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "更多选项",
                    tint = textColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(37.dp))
        // 单词展示区
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "resort",
                color = textColor,
                fontSize = 46.sp,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    modifier = Modifier
                    .width(38.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.5f))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = soundType,
                            color = Color.LightGray,
                            fontSize = 18.sp,
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "播放读音",
                            tint = textColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "/nˈzɜːrt/",
                    color = Color.LightGray,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            // 例句
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.5f))
            ) {
                Text(
                    text = "This place is just so charming, the perfect resort",
                    color = textColor,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "请从下列 4 个选项中选择正确词义",
            color = hintColor,
            fontSize = 13.sp,
        )
        Column {
            words.forEachIndexed { index, option ->
                Spacer(modifier = Modifier.height(13.dp))
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selected) {
                            when (index) {
                                rightIndex -> Color(0xFF10885B)
                                else -> Color(0xFF8A0303)
                            }
                        } else Color.Transparent
                    ),
                    modifier = Modifier
                        .width(394.dp)
                        .height(80.dp)
                        .clickable { selected = true }
                        .border(
                            width = 2.dp,
                            color = if (selected) Color.Transparent else Color.White.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Text(
                        text = if(selected) option else wordTypes[index],
                        color =if(selected)  textColor else darkTextColor,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start=18.dp,top=18.dp)
                    )
                    Text(
                        text = if(selected) wordTypes[index]+wordMeanings[index] else wordMeanings[index],
                        color = if(selected) darkTextColor else textColor,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = 18.dp, bottom = 18.dp)
                    )
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 93.dp)
        ) {
            Button(
                onClick = {
                    if(selected){
                        //TODO next word
                    }
                    else{
                        selected=true
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.3f),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(32.dp),
                modifier =Modifier.width(105.dp)
            ) {
                Text(
                    text = if(selected) "继续" else "看答案",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold)
            }
        }
    }
}