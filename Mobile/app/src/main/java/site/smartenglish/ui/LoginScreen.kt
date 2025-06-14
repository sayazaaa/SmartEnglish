package site.smartenglish.ui

import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.lifecycle.ViewModelStoreOwner
import kotlinx.coroutines.delay
import site.smartenglish.R
import site.smartenglish.ui.compose.WideButton
import site.smartenglish.ui.viewmodel.AccountViewmodel
import site.smartenglish.ui.viewmodel.SnackBarViewmodel

@Composable
fun LoginScreen(
    navigateToRegister: () -> Unit = {},
    navigateToHome: () -> Unit = {},
    navigateToResetPassword: () -> Unit = {},
    viewModel: AccountViewmodel = hiltViewModel(),
    snackBarViewmodel: SnackBarViewmodel = hiltViewModel(LocalActivity.current as ViewModelStoreOwner)
) {
    val density = LocalDensity.current
    // 获取UI状态
    val uiState by viewModel.loginUiState.collectAsState()

    var phoneInput by remember { mutableStateOf("") }
    var verificationInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

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

    fun validatePassword(password: String): String? {
        return when {
            password.isEmpty() -> "密码不能为空"
            password.length < 6 -> "密码长度至少6位"
            else -> null
        }
    }

    // 监听注册状态变化
    LaunchedEffect(uiState.loginSuccess, uiState.error) {
        when {
            uiState.loginSuccess -> {
                navigateToHome()
                snackBarViewmodel.showSnackbar("登录成功")
            }

            uiState.error != null -> {
                snackBarViewmodel.showSnackbar(uiState.error ?: "登录失败，请重试")
                Log.e("LoginScreen", uiState.error!!)
                viewModel.clearLoginError()
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

                    if(!uiState.byPassword){
                        TextButton(
                            onClick = {
                                phoneError = validatePhone(phoneInput)
                                if (phoneError == null && canRequestCode) {
                                    viewModel.sendLoginVerificationCode(phoneInput)
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
                }

                Spacer(Modifier.height(with(density) { 22.dp - 8.sp.toDp() }))

                // 密码输入框
                OutlinedTextField(
                    value = if(uiState.byPassword) passwordInput else verificationInput,
                    onValueChange = {
                        passwordInput = if(uiState.byPassword) it else passwordInput
                        verificationInput = if(!uiState.byPassword) it else verificationInput
                        passwordError =if(uiState.byPassword) validatePassword(passwordInput) else validateCode(passwordInput)
                    },
                    label = { Text(if(uiState.byPassword)"密码" else "验证码") },
                    placeholder = { Text(if(uiState.byPassword)"请输入密码" else "请输入验证码") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .width(356.dp)
                        .height(64.dp),
                    visualTransformation = if (isPasswordVisible||!uiState.byPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    isError = passwordError != null,
                    trailingIcon = {
                        if(uiState.byPassword){
                            androidx.compose.material3.IconButton(
                                onClick = { isPasswordVisible = !isPasswordVisible }
                            ) {
                                androidx.compose.material3.Icon(
                                    imageVector = if (isPasswordVisible)
                                        Icons.Default.Visibility
                                    else
                                        Icons.Default.VisibilityOff,
                                    contentDescription = if (isPasswordVisible) "隐藏密码" else "显示密码"
                                )
                            }
                        }
                    }
                )
                Spacer(Modifier.height(10.dp))
                //切换按钮
                Row(
                    modifier = Modifier
                        .height(30.dp)
                        .width(300.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = if(uiState.byPassword) "验证码登录" else "密码登录",
                        modifier = Modifier
                            .width(70.dp)
                            .height(20.dp)
                            .clickable {
                                isPasswordVisible = false
                                viewModel.changeWayOfLogin()
                            },
                        color = Color.Black,
                        fontSize = 12.sp
                    )
                }
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

                // 登录按钮
                WideButton(
                    text = if (uiState.isLoading) "登录中..." else "登录",
                    onClick = {
                        // 验证所有输入
                        phoneError = validatePhone(phoneInput)

                        if (uiState.byPassword) {
                            passwordError = validatePassword(passwordInput)
                            if (passwordError == null) {
                                codeError = null
                            }
                        } else {
                            codeError = validateCode(verificationInput)
                            if (codeError == null) {
                                passwordError = null
                            }
                        }

                        if (phoneError == null && codeError == null && passwordError == null) {
                            if (uiState.byPassword) {
                                viewModel.loginWithPassword(phoneInput, passwordInput)
                            } else {
                                viewModel.loginWithVerification(phoneInput, verificationInput)
                            }
                        }
                    },
                    fontsize = 16,
                    color = Color(0xFFFFFEFD)
                )
                // 注册按钮
                TextButton(
                    onClick = { navigateToRegister() },
                    modifier = Modifier.padding(0.dp)
                ) {
                    Text(
                        "注册",
                        fontSize = 14.sp, color = Color.Black
                    )
                }
                // 忘记密码按钮
                TextButton(
                    onClick = { navigateToResetPassword() }, modifier = Modifier.padding(0.dp)
                ) {
                    Text(
                        "忘记密码？", fontSize = 14.sp, color = Color.Black
                    )
                }
            }
        }
    }
}
