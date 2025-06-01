package site.smartenglish.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import site.smartenglish.manager.SessionManager
import site.smartenglish.ui.viewmodel.AccountViewmodel


@Serializable
object Login

@Serializable
object Register

@Serializable
object ResetPassword

@Serializable
object Home

@Serializable
object Profile

@Composable
fun MainNav(
    accountViewmodel: AccountViewmodel = hiltViewModel()
) {
    val navController = rememberNavController()
    val authState by accountViewmodel.authState.collectAsState()

    // 根据token状态决定起始目的地
    val startDestination = remember {
        if (accountViewmodel.hasToken()) Home else Login
    }

    // 监听认证状态变化
    LaunchedEffect(authState) {
        if (authState == SessionManager.AuthState.UNAUTHORIZED) {
            navController.navigate(Login) {
                // 清除返回栈，防止用户按返回按钮回到需要授权的页面
                popUpTo(0) { inclusive = true }
            }
            accountViewmodel.resetAuthState()
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Login> {
            LoginScreen(
                navigateToRegister = { navController.navigate(Register) },
                navigateToResetPassword = { navController.navigate(ResetPassword) },
                navigateToHome = {
                    navController.navigate(Home) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                })
        }
        composable<Register> { RegisterScreen(
            navigateToLogin = { navController.navigate(Login) { popUpTo(0) { inclusive = true } } },
        ) }
        composable<ResetPassword> { ResetPasswordScreen(
            navigateBack = { navController.navigate(Login) { popUpTo(0) { inclusive = true } } },
            navigateToLogin = { navController.navigate(Login) { popUpTo(0) { inclusive = true } } }
        ) }
        composable<Home> { HomeScreen(

        ) }
        composable<Profile> { ProfileScreen() }



    }
}
