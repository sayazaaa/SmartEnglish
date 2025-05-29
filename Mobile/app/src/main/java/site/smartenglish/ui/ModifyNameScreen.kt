package site.smartenglish.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import site.smartenglish.ui.compose.CenterAlignedBackArrowTopAppBar
import site.smartenglish.ui.compose.WideButton
import site.smartenglish.ui.theme.Orange
import site.smartenglish.ui.theme.White

@Composable
fun ModifyNameScreen(

) {
    // 传入原昵称
    var name by remember { mutableStateOf("我爱学英语") }
    Scaffold(
        topBar = {
            CenterAlignedBackArrowTopAppBar(
                title = "修改昵称",
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BasicTextField(
                    value = name,
                    onValueChange = { newName ->
                        name = newName
                    },
                    modifier = Modifier
                        .width(342.dp)
                        .padding(top = 61.dp),
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        lineHeight = 21.sp,
                        color = White,
                        textAlign = TextAlign.Start,
                    ),
                    cursorBrush = SolidColor(Orange),
                    decorationBox = { innerTextField ->
                        Column {
                            // 文本字段
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                innerTextField()
                            }
                            // 底线
                            Spacer(modifier = Modifier.height(19.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(Color(0xFF3C4154))
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(35.dp))
                WideButton(
                    text = "确认修改",
                    onClick = {},
                    width = 342,
                    height = 53,
                    fontsize = 19
                )
            }


        }
    )
}