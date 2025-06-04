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


    // 用户资料状态
    private val _userProfile = MutableStateFlow<GetProfileResponse?>(null)
    val userProfile: StateFlow<GetProfileResponse?> = _userProfile.asStateFlow()



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




}