package site.smartenglish.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
fun ResetPasswordScreen(
    navigateToLogin: () -> Unit = {},
    viewModel: AccountViewmodel = hiltViewModel()
) {
    val density = LocalDensity.current
    // 获取UI状态 应该用别的状态，不过好像这个也能用
    val uiState by viewModel.registerUiState.collectAsState()

    // Snackbar状态管理
    val snackbarHostState = remember { SnackbarHostState() }

    var phoneInput by remember { mutableStateOf("") }
    var verificationInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var rePasswordInput by remember { mutableStateOf("") }
    var isPasswordVisible0 by remember { mutableStateOf(false) }
    var isPasswordVisible1 by remember { mutableStateOf(false) }

    // 错误状态
    var phoneError by remember { mutableStateOf<String?>(null) }
    var codeError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }


    // 验证码倒计时
    var canRequestCode by remember { mutableStateOf(true) }
    var countdown by remember { mutableIntStateOf(0) }

    // 验证函数
    fun validatePhone(phone: String): String? {
        return when {
            phone.isEmpty() -> "手机号不能为空"
            !phone.matches(Regex("^\\d{11}$")) -> "请输入11位手机号码"
            else -> null
        }
    }

    fun validateCode(code: String): String? {
        return when {
            code.isEmpty() -> "验证码不能为空"
            !code.matches(Regex("^\\d{4}$")) -> "请输入4位验证码"
            else -> null
        }
    }

    fun validatePassword(password: String,rePassword: String): String? {
        return when {
            password.isEmpty() -> "密码不能为空"
            password.length < 6 -> "密码长度至少6位"
            !rePassword.equals(password) -> "两次输入的密码不一致"
            else -> null
        }
    }

    // 监听注册状态变化
    LaunchedEffect(uiState.registerSuccess, uiState.error) {
        when {
            uiState.registerSuccess -> {
                snackbarHostState.showSnackbar("注册成功！")
                navigateToLogin()
            }

            uiState.error != null -> {
                snackbarHostState.showSnackbar(uiState.error ?: "注册失败，请重试")
                Log.e("RegisterScreen", uiState.error!!)
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
        modifier = Modifier
            .background(Color(0xFFFFFCF8)),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentColor = Color(0xFFFFFCF8),
    ) { i ->
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
                        onValueChange = { s ->
                            phoneInput = s.take(11).filter { it.isDigit() }
                        },
                        label = { Text("手机号") },
                        placeholder = { Text("请输入手机号") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .width(356.dp)
                            .height(64.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        isError = phoneError != null,
                    )
                    TextButton(
                        onClick = {
                            phoneError = validatePhone(phoneInput)
                            if (phoneError == null && canRequestCode) {
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

                // 验证码输入框

                OutlinedTextField(
                    value = verificationInput,
                    onValueChange = { s ->
                        verificationInput = s.take(4).filter { it.isDigit() }
//                    codeError = validateCode(verificationInput)
                    },
                    label = { Text("验证码") },
                    placeholder = { Text("请输入验证码") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .width(356.dp)
                        .height(64.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = codeError != null,
                )

                Spacer(Modifier.height(with(density) { 22.dp - 8.sp.toDp() }))

                OutlinedTextField(
                    value = passwordInput,
                    onValueChange = {
                        passwordInput = it
                        rePasswordInput = ""
                        passwordError = ""
                    },
                    label = { Text("密码") },
                    placeholder = { Text("请输入密码") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .width(356.dp)
                        .height(64.dp),
                    visualTransformation = if (isPasswordVisible1) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    isError = passwordError != null,
                    trailingIcon = {
                        androidx.compose.material3.IconButton(
                            onClick = { isPasswordVisible1 = !isPasswordVisible1 }
                        ) {
                            androidx.compose.material3.Icon(
                                imageVector = if (isPasswordVisible1)
                                    Icons.Default.Visibility
                                else
                                    Icons.Default.VisibilityOff,
                                contentDescription = if (isPasswordVisible1) "隐藏密码" else "显示密码"
                            )
                        }
                    }
                )
                Spacer(Modifier.height(with(density) { 22.dp - 8.sp.toDp() }))
                // 重复密码输入框
                OutlinedTextField(
                    value = rePasswordInput,
                    onValueChange = {
                        rePasswordInput = it
                        passwordError = validatePassword(passwordInput,rePasswordInput)
                    },
                    label = { Text("重复密码") },
                    placeholder = { Text("请再次输入密码") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .width(356.dp)
                        .height(64.dp),
                    visualTransformation = if (isPasswordVisible0) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    isError = passwordError != null,
                    trailingIcon = {
                        androidx.compose.material3.IconButton(
                            onClick = { isPasswordVisible0 = !isPasswordVisible0 }
                        ) {
                            androidx.compose.material3.Icon(
                                imageVector = if (isPasswordVisible0)
                                    Icons.Default.Visibility
                                else
                                    Icons.Default.VisibilityOff,
                                contentDescription = if (isPasswordVisible0) "隐藏密码" else "显示密码"
                            )
                        }
                    }
                )

                Spacer(Modifier.height(10.dp))
                Row(modifier = Modifier.height(30.dp), horizontalArrangement = Arrangement.Center) {
                    // 错误提示
                    phoneError?.let {
                        Text(
                            text = it,
                            color = Color.Red,
                            fontSize = 12.sp,
                        )
                    }
                    codeError?.let {
                        phoneError?.let {
                            Spacer(Modifier.width(10.dp))
                        }
                        Text(
                            text = it,
                            color = Color.Red,
                            fontSize = 12.sp,
                        )
                    }
                    passwordError?.let {
                        if (phoneError != null || codeError != null) {
                            Spacer(Modifier.width(10.dp))
                        }
                        Text(
                            text = it,
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(Modifier.height(with(density) { 40.dp - 8.sp.toDp() }))

                // 注册按钮
                WideButton(
                    text = if (uiState.isLoading) "登录中..." else "登录",
                    onClick = {
                        // 验证所有输入
                        phoneError = validatePhone(phoneInput)
                        codeError = validateCode(verificationInput)
                        passwordError = validatePassword(passwordInput,rePasswordInput)

                        // 只有全部验证通过才提交
                        //TODO reset logics
//                        if (phoneError == null && codeError == null && passwordError == null) {
//                            viewModel.register(phoneInput, verificationInput, passwordInput)
//                        }
                    },
                    fontsize = 16,
                    color = Color(0xFFFFFEFD)
                )
            }
        }
    }
}
