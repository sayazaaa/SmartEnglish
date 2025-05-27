package site.smartenglish.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable


@Serializable
object Login

@Serializable
object Register

@Composable
fun MainNav() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Register
    ) {
        composable<Login> {}
        composable<Register> { RegisterScreen() }


    }
}
