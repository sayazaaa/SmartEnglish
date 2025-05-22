package site.smartenglish.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.compose.rememberConstraintsSizeResolver
import site.smartenglish.ui.compose.GlassButton


@Composable
fun HomeLayout(
    learnNum: Int = 0, reviewNum: Int = 0, bg: String
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 背景图片
        val sizeResolver = rememberConstraintsSizeResolver()
        val bgPainter = rememberAsyncImagePainter(model = bg)
        Image(
            painter = bgPainter,
            contentDescription = null,
            modifier = Modifier.then(sizeResolver),
            contentScale = ContentScale.Crop,
        )
        // 半透明遮罩
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.2f))
        )

        // 顶部搜索栏
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 16.dp, end = 16.dp)
        ) {
            // 顶部图标 + 搜索框
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { }) {
                    AsyncImage(
                        model = "https://temp.im/50x50/FF0000/FFFFFF?text=head",
                        contentDescription = "Profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(40.dp)
                            .height(40.dp)
                            .background(
                                Color.White, shape = RoundedCornerShape(20.dp)
                            )
                    )
                }

                Box {
                    TextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("Search", color = Color.Gray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        colors = TextFieldDefaults.colors(
//                            focusedIndicatorColor = Color.Transparent,
//                            unfocusedIndicatorColor = Color.Transparent,
//                            focusedContainerColor = Color.Transparent,
//                            unfocusedContainerColor = Color.Transparent,
                        )
                    )
                }
            }
        }

        // 中间标题

        Text(
            text = "Tranquility",
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-240).dp)
        )
        // Learn 和 Review
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            GlassButton(
                modifier = Modifier
                    .width(190.dp)
                    .height(77.dp),
                onClick = {},
                text = "Learn",
                number = learnNum.toString(),
                backgroundImagePainter = bgPainter,
            )
            GlassButton(
                modifier = Modifier
                    .width(190.dp)
                    .height(77.dp),
                onClick = {},
                text = "Review",
                number = reviewNum.toString(),
                backgroundImagePainter = bgPainter,
            )
        }
    }
}


@Preview(showBackground = false)
@Composable
fun HomeLayoutPreview() {
    HomeLayout(bg = "https://images.pexels.com/photos/3490963/pexels-photo-3490963.jpeg")
}