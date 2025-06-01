package site.smartenglish.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import site.smartenglish.ui.compose.CenterAlignedBackArrowTopAppBar
import site.smartenglish.ui.compose.WideButton
import site.smartenglish.ui.theme.Orange
import site.smartenglish.ui.theme.White
import site.smartenglish.ui.viewmodel.UserViewmodel

@Composable
fun UserFeedbackScreen(
    navigateBack: () -> Unit = {},
    viewmodel: UserViewmodel = hiltViewModel()
) {
    var feedback by remember { mutableStateOf("") }
    Scaffold(topBar = {
        CenterAlignedBackArrowTopAppBar(
            title = "用户反馈",
            onBackClick = navigateBack
        )
    }, content = { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "反馈内容",
                color = White,
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 61.dp).width(400.dp),
                fontWeight = FontWeight.W500
            )
            BasicTextField(
                value = feedback,
                onValueChange = { newFeedback ->
                    feedback = newFeedback
                },
                modifier = Modifier
                    .width(400.dp)
                    .height(314.dp)
                    .padding(top = 20.dp)
                    ,
                singleLine = false,
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    color = White,
                    textAlign = TextAlign.Start,
                ),
                cursorBrush = SolidColor(Orange),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                width = 1.dp,
                                color = Color(0xFF878278),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .background(Color(0xFF2C2F3E))
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        innerTextField()
                    }
                })
            Spacer(modifier = Modifier.height(35.dp))
            WideButton(
                text = "提交反馈", onClick = {
                    if (feedback.isNotBlank()) {
                        //toDO 提交反馈逻辑
                        navigateBack()
                    }
                }, width = 342, height = 53, fontsize = 19
            )
        }
    })
}