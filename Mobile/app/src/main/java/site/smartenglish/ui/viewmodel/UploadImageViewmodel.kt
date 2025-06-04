package site.smartenglish.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import site.smartenglish.remote.TencentCloudService
import site.smartenglish.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class UploadImageViewmodel @Inject constructor(
    private val cloudService: TencentCloudService
) : ViewModel() {
    sealed class UploadState {
        data object Idle : UploadState()
        data object Progress : UploadState()
        data class Success(val imageUrl: String) : UploadState()
        data class Error(val message: String) : UploadState()
    }
    // 上传状态
    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState: StateFlow<UploadState> = _uploadState.asStateFlow()

    // 上传图片
    fun uploadImage(uri: Uri) {
        viewModelScope.launch {
            try {
                _uploadState.value = UploadState.Progress

                // 上传图片
                val imageUrl = cloudService.uploadImage(uri)

                // 更新状态
                _uploadState.value = UploadState.Success(imageUrl)
            } catch (e: Exception) {
                _uploadState.value = UploadState.Error("上传失败: ${e.message ?: "未知错误"}")
            }
        }
    }

    // 重置上传状态
    fun resetUploadState() {
        _uploadState.value = UploadState.Idle
    }


}