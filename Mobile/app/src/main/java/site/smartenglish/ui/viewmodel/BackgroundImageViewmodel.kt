package site.smartenglish.ui.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import site.smartenglish.repository.UtilRepository
import javax.inject.Inject

@HiltViewModel
class BackgroundImageViewmodel @Inject constructor(
    private val utilRepository: UtilRepository
) : ViewModel() {
    //bg
    private val _imageUrl = MutableStateFlow("https://images.pexels.com/photos/32241306/pexels-photo-32241306.jpeg")
    val imageUrl: StateFlow<String> = _imageUrl.asStateFlow()

    // 默认的灰色Bitmap
    private val defaultBitmap = createBitmap(1, 1).apply {
        eraseColor(Color.Gray.toArgb())
    }

    // Bitmap状态
    private val _backgroundBitmap = MutableStateFlow(defaultBitmap)
    val backgroundBitmap: StateFlow<Bitmap> = _backgroundBitmap.asStateFlow()

    // 加载状态
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // 错误信息
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init{
        loadBackgroundImage(_imageUrl.value)
    }


    fun loadBackgroundImage(imageUrl: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null

                utilRepository.loadImageAsBitmap(imageUrl).fold(onSuccess = { bitmap ->
                    _backgroundBitmap.value = bitmap
                    _isLoading.value = false
                }, onFailure = { exception ->
                    Log.e("HomeViewModel", "图片加载失败", exception)
                    _errorMessage.value = "图片加载失败: ${exception.message}"
                    _isLoading.value = false
                })
            } catch (e: Exception) {
                Log.e("HomeViewModel", "加载图片时发生错误", e)
                _errorMessage.value = "加载图片时发生错误: ${e.message}"
                _isLoading.value = false
            }
        }
    }
}
