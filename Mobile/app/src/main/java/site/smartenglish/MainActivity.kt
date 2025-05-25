package site.smartenglish

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import site.smartenglish.ui.RegisterScreen
import site.smartenglish.ui.theme.SmartEnglishTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartEnglishTheme {
                Surface {
                    RegisterScreen()
                }
            }
        }
    }
}

@HiltAndroidApp
class MainApp:Application()