package site.smartenglish.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import site.smartenglish.R
import site.smartenglish.ui.compose.WideButton
import site.smartenglish.ui.viewmodel.AccountViewmodel

@Composable
fun RegisterScreen(
    navigateToLogin: () -> Unit = {},
    viewModel: AccountViewmodel = hiltViewModel()
) {
    val density = LocalDensity.current
    // 获取UI状态
    val uiState by viewModel.registerUiState.collectAsState()

    // Snackbar状态管理
    val snackbarHostState = remember { SnackbarHostState() }

    var phoneInput by remember { mutableStateOf("") }
    var verificationInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    val isPasswordVisible by remember { mutableStateOf(false) }

    // 验证码倒计时
    var canRequestCode by remember { mutableStateOf(true) }
    var countdown by remember { mutableIntStateOf(0) }

    // 监听注册状态变化
    LaunchedEffect(uiState.registerSuccess, uiState.error) {
        when {
            uiState.registerSuccess -> {
                snackbarHostState.showSnackbar("注册成功！")
                navigateToLogin()
            }
            uiState.error != null -> {
                snackbarHostState.showSnackbar(uiState.error ?: "注册失败，请重试")
                Log.e("RegisterScreen",uiState.error!!)
                viewModel.clearRegisterError()
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

    Scaffold(
        modifier = Modifier.background(Color(0xFFFFFCF8)),
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) {  i ->
    Box(
        modifier = Modifier
            .background(Color(0xFFFFFCF8))
            .fillMaxSize()
            .padding(i)
    ) {
        //logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .width(70.dp)
                .height(240.dp)
                .offset(y = 123.dp)
                .align(Alignment.TopCenter),
        )

        // 输入框
        Column(
            modifier = Modifier
                .padding(horizontal = 36.dp)
                .offset(y = with(density) { 464.dp - 8.sp.toDp() }),
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

            Spacer(Modifier.height(with(density) { 22.dp - 8.sp.toDp() }))

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

            Spacer(Modifier.height(with(density) { 22.dp - 8.sp.toDp() }))

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

            Spacer(Modifier.height(with(density) { 80.dp - 8.sp.toDp() }))

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
                onClick = { navigateToLogin() },
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
