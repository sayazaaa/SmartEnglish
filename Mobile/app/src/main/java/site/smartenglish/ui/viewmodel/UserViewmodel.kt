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
import site.smartenglish.remote.data.GetProfileResponse
import site.smartenglish.repository.UserRepository
import javax.inject.Inject



@HiltViewModel
class UserViewmodel @Inject constructor(
    private val userRepository: UserRepository,
    private val cloudService: TencentCloudService
) : ViewModel() {
    sealed class UploadState {
        data object Idle : UploadState()
        data object Progress : UploadState()
        data class Success(val imageUrl: String) : UploadState()
        data class Error(val message: String) : UploadState()
    }

    // 用户资料状态
    private val _userProfile = MutableStateFlow<GetProfileResponse?>(null)
    val userProfile: StateFlow<GetProfileResponse?> = _userProfile.asStateFlow()

    // 上传状态
    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState: StateFlow<UploadState> = _uploadState.asStateFlow()

    init {
        getProfile()
    }

    fun getProfile() {
        viewModelScope.launch {
            try {
                val profile = userRepository.getProfile()
                _userProfile.value = profile
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun changeProfile(
        name: String? = null,
        description: String? = null,
        avatar: String? = null,
        wordbookId: Int? = null
    ) {
        viewModelScope.launch {
            try {
                val updatedProfile = userRepository.changeProfile(name, description, avatar, wordbookId)
                _userProfile.value = updatedProfile
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun changeName(name: String) {
        viewModelScope.launch {
            try {
                val updatedProfile = userRepository.changeProfile(name = name)
                _userProfile.value = updatedProfile
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun changeDescription(description: String) {
        viewModelScope.launch {
            try {
                val updatedProfile = userRepository.changeProfile(description = description)
                _userProfile.value = updatedProfile
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun changeAvatar(avatar: String) {
        viewModelScope.launch {
            try {
                val updatedProfile = userRepository.changeProfile(avatar = avatar)
                _userProfile.value = updatedProfile
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun changeWordbookId(wordbookId: Int) {
        viewModelScope.launch {
            try {
                val updatedProfile = userRepository.changeProfile(wordbookId = wordbookId)
                _userProfile.value = updatedProfile
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



    // 上传图片
    fun uploadImage(uri: Uri) {
        viewModelScope.launch {
            try {
                _uploadState.value = UploadState.Progress

                // 上传图片
                val imageUrl = cloudService.uploadImage(uri)

                // 更新用户资料
                changeProfile(avatar = imageUrl)

                // 更新状态
                _uploadState.value = UploadState.Success(imageUrl)
            } catch (e: Exception) {
                _uploadState.value = UploadState.Error("上传失败: ${e.message ?: "未知错误"}")
            }
        }
    }

    // 发送反馈
    fun sendFeedback(content: String) {
        viewModelScope.launch {
            try {
                userRepository.sendFeedback(content)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 重置上传状态
    fun resetUploadState() {
        _uploadState.value = UploadState.Idle
    }



}