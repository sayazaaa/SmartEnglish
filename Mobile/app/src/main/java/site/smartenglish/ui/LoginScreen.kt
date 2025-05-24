package site.smartenglish.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import site.smartenglish.R


@Composable
fun LoginScreen() {
    var currentState by remember { mutableStateOf("LogoOnly") }

    LaunchedEffect(Unit) {
        delay(2000) // 2秒延迟
        currentState = "InputsVisible"
    }

    val transition = updateTransition(targetState = currentState, label = "")

    val logoOffsetY by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = 800, easing = LinearEasing)
        }
    ) {
        when (it) {
            "LogoOnly" -> 200f
            "InputsVisible" -> 50f
            else ->0f
        }
    }

    val inputAlpha by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = 800, delayMillis = 800, easing = LinearEasing)
        }
    ) {
        if (it == "InputsVisible") 1f else 0f
    }

    var phoneInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 图标
        Image(
            painter = painterResource(id = R.drawable.title),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(400.dp)
                .offset(y = logoOffsetY.dp)
        )

        // 输入框
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(inputAlpha)
                .padding(top = 64.dp, start = 16.dp, end = 16.dp)
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 手机号输入框
                OutlinedTextField(
                    value = phoneInput,
                    onValueChange = { phoneInput = it },
                    label = { Text("手机号") },
                    placeholder = { Text("请输入手机号") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                // 密码输入框
                OutlinedTextField(
                    value = passwordInput,
                    onValueChange = { passwordInput = it },
                    label = { Text("密码") },
                    placeholder = { Text("请输入密码") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//                    trailingIcon = {
//                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
//                            Icon(
//                                painter = painterResource(id = if (isPasswordVisible) R.drawable.ic_eye else R.drawable.ic_eye_off),
//                                contentDescription = if (isPasswordVisible) "隐藏密码" else "显示密码"
//                            )
//                        }
//                    }
                )

                // 登录按钮
                Button(
                    onClick = { /* 处理登录逻辑 */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                ) {
                    Text("登录")
                }
            }
        }
    }
}