package site.smartenglish

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import site.smartenglish.ui.DictationScreen
import site.smartenglish.ui.HomeScreen
import site.smartenglish.ui.LearnWordFinishedScreen
import site.smartenglish.ui.LearnWordScreen
import site.smartenglish.ui.MainScreen
import site.smartenglish.ui.WordDetailScreen
import site.smartenglish.ui.theme.SmartEnglishTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 初始化启动画面
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartEnglishTheme {
                Surface {
                  DictationScreen()
                }
            }
        }
    }
}

@HiltAndroidApp
class MainApp : Application()