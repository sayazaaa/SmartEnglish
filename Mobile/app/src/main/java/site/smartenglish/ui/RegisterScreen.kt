package site.smartenglish.ui

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import site.smartenglish.R
import site.smartenglish.ui.compose.WideButton
import site.smartenglish.ui.viewmodel.AccountViewmodel
import site.smartenglish.ui.viewmodel.LoginUiState

@Composable
fun RegisterScreen(viewModel: AccountViewmodel = hiltViewModel()) {
    val density = LocalDensity.current
    // 获取UI状态
    val uiState by viewModel.registerUiState.collectAsState()

    // Snackbar状态管理
    val snackbarHostState = remember { SnackbarHostState() }

    var currentState by remember { mutableStateOf("InputsVisible") }
    var phoneInput by remember { mutableStateOf("") }
    var verificationInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    val isPasswordVisible by remember { mutableStateOf(false) }

    // 验证码倒计时
    var canRequestCode by remember { mutableStateOf(true) }
    var countdown by remember { mutableStateOf(0) }

    //spacer
    val spacerHeight = with(density) { 22.dp - 8.sp.toDp() }

    // 监听注册状态变化
    LaunchedEffect(uiState.registerSuccess, uiState.error) {
        when {
            uiState.registerSuccess -> {
                snackbarHostState.showSnackbar("注册成功！")
                // TODO 在这里添加导航到登录页面的逻辑
            }
            uiState.error != null -> {
                snackbarHostState.showSnackbar(uiState.error ?: "注册失败，请重试")
                Log.e("RegisterScreen",uiState.error!!)
                viewModel.clearRegisterError() // 需要在ViewModel中添加此方法
            }
        }
    }

    LaunchedEffect(countdown) {
        if (countdown > 0) {
            delay(1000)
            countdown--
        } else {
            canRequestCode = true
        }
    }

    LaunchedEffect(Unit) {
        delay(2000) // 2秒延迟
        currentState = "InputsVisible"
    }

    // 动画相关代码
    val transition = updateTransition(targetState = currentState, label = "")

    val logoOffsetY by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = 800, easing = LinearEasing)
        }
    ) {
        when (it) {
            "LogoOnly" -> 323f
            "InputsVisible" -> 123f
            else -> 0f
        }
    }

    val inputAlpha by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = 800, delayMillis = 800, easing = LinearEasing)
        }
    ) {
        if (it == "InputsVisible") 1f else 0f
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) {  i ->
    Box(
        modifier = Modifier
            .background(Color(0xFFFFFCF8))
            .fillMaxSize()
            .padding(i)
    ) {
        // 图标
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .width(70.dp)
                .height(240.dp)
                .offset(y = logoOffsetY.dp)
                .align(Alignment.TopCenter),
        )

        // 输入框
        Column(
            modifier = Modifier
                .padding(horizontal = 36.dp)
                .offset(y = with(density) { 464.dp - 8.sp.toDp() })
                .alpha(inputAlpha),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 手机号输入框
            Box(Modifier.height(64.dp)) {
                OutlinedTextField(
                    value = phoneInput,
                    onValueChange = { phoneInput = it },
                    label = { Text("手机号") },
                    placeholder = { Text("请输入手机号") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .width(356.dp)
                        .height(64.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                TextButton(
                    onClick = {
                        if (phoneInput.isNotEmpty() && canRequestCode) {
                            viewModel.sendRegisterVerificationCode(phoneInput)
                            canRequestCode = false
                            countdown = 60
                        }
                    },
                    enabled = phoneInput.isNotEmpty() && canRequestCode && !uiState.isLoading,
                    modifier = Modifier
                        .padding(top = with(density) { 8.sp.toDp() })
                        .height(64.dp)
                        .align(Alignment.CenterEnd)
                ) {
                    Text(
                        if (countdown > 0) "${countdown}s" else "获取验证码",
                        modifier = Modifier.align(Alignment.CenterVertically),
                        fontSize = 14.sp,
                    )
                }
            }

            Spacer(Modifier.height(spacerHeight))

            // 验证码输入框和发送按钮

            OutlinedTextField(
                value = verificationInput,
                onValueChange = { verificationInput = it },
                label = { Text("验证码") },
                placeholder = { Text("请输入验证码") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .width(356.dp)
                    .height(64.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(Modifier.height(spacerHeight))

            // 密码输入框
            OutlinedTextField(
                value = passwordInput,
                onValueChange = { passwordInput = it },
                label = { Text("密码") },
                placeholder = { Text("请输入密码") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .width(356.dp)
                    .height(64.dp),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(Modifier.height(spacerHeight + 52.dp))

            // 注册按钮
            WideButton(
                text = if (uiState.isLoading) "注册中..." else "注册",
                onClick = {
                    if (phoneInput.isNotEmpty() && verificationInput.isNotEmpty() && passwordInput.isNotEmpty()) {
                        viewModel.register(phoneInput, verificationInput, passwordInput)
                    }
                },
                fontsize = 16,
                color = Color(0xFFFFFEFD)
            )
            // 登录按钮
            TextButton(
                onClick = {/*TODO*/ },
                modifier = Modifier.padding(0.dp)
            ) {
                Text(
                    "登录",
                    fontSize = 14.sp,
                )
            }
        }
    }}
}
