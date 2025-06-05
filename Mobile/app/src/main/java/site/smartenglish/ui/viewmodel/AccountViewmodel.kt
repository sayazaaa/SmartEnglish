package site.smartenglish.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import site.smartenglish.manager.SessionManager
import site.smartenglish.repository.AccountRepository
import site.smartenglish.repository.UserRepository
import javax.inject.Inject


// 登录界面状态
data class LoginUiState(
    val isLoading: Boolean = false,
    val verificationSent: Boolean = false,
    val loginSuccess: Boolean = false,
    val error: String? = null,
    val byPassword: Boolean = true
)

//注册页面状态
data class RegisterUiState(
    val isLoading: Boolean = false,
    val verificationSent: Boolean = false,
    val registerSuccess: Boolean = false,
    val error: String? = null
)

//重置密码页面状态
data class ResetPasswordUiState(
    val isLoading: Boolean = false,
    val verificationSent: Boolean = false,
    val resetSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AccountViewmodel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    // UI状态
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()
    private val _registerUiState = MutableStateFlow(RegisterUiState())
    val registerUiState: StateFlow<RegisterUiState> = _registerUiState.asStateFlow()
    private val _resetPasswordUiState = MutableStateFlow(ResetPasswordUiState())
    val resetPasswordUiState: StateFlow<ResetPasswordUiState> = _resetPasswordUiState.asStateFlow()

    fun hasToken(): Boolean = accountRepository.isAuthenticated()

    // 认证状态
    val authState: StateFlow<SessionManager.AuthState> = accountRepository.authState
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            SessionManager.AuthState.AUTHENTICATED
        )

    // 切换登录方式
    fun changeWayOfLogin(){
        viewModelScope.launch {
            try {
                _loginUiState.update {
                    it.copy(byPassword = !it.byPassword)
                }
            } catch (e: Exception) {
                _loginUiState.update {
                    it.copy(
                        error = e.message
                    )
                }
            }
        }
    }

    // 发送验证码
    fun sendLoginVerificationCode(phone: String) {
        viewModelScope.launch {
            try {
                _loginUiState.update { it.copy(isLoading = true) }
                accountRepository.getVerificationCode(phone)
                _loginUiState.update {
                    it.copy(
                        isLoading = false,
                        verificationSent = true
                    )
                }
            } catch (e: Exception) {
                _loginUiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun sendRegisterVerificationCode(phone: String) {
        viewModelScope.launch {
            try {
                _registerUiState.update { it.copy(isLoading = true) }
                accountRepository.getVerificationCode(phone)
                _registerUiState.update {
                    it.copy(
                        isLoading = false,
                        verificationSent = true
                    )
                }
            } catch (e: Exception) {
                _registerUiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    // 登录
    fun loginWithVerification(phone: String, verification: String) {
        viewModelScope.launch {
            try {
                _loginUiState.update { it.copy(isLoading = true) }
                accountRepository.loginWithVerification(phone, verification)
                _loginUiState.update {
                    it.copy(
                        isLoading = false,
                        loginSuccess = true
                    )
                }
            } catch (e: Exception) {
                _loginUiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun loginWithPassword(phone: String, password: String) {
        viewModelScope.launch {
            try {
                _loginUiState.update { it.copy(isLoading = true) }
                accountRepository.loginWithPassword(phone, password)
                _loginUiState.update {
                    it.copy(
                        isLoading = false,
                        loginSuccess = true
                    )
                }
            } catch (e: Exception) {
                _loginUiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    //注册
    fun register(phone: String, verification: String, password: String) {
        viewModelScope.launch {
            try {
                _registerUiState.update { it.copy(isLoading = true) }
                accountRepository.register(phone, verification, password)
                _registerUiState.update {
                    it.copy(
                        isLoading = false,
                        registerSuccess = true
                    )
                }
            } catch (e: Exception) {
                _registerUiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    // 重置密码
    fun resetPassword(phone: String, verification: String, newPassword: String) {
        viewModelScope.launch {
            try {
                _resetPasswordUiState.update { it.copy(isLoading = true) }
                accountRepository.resetPassword(phone, verification, newPassword)
                _resetPasswordUiState.update {
                    it.copy(
                        isLoading = false,
                        resetSuccess = true
                    )
                }
            } catch (e: Exception) {
                _resetPasswordUiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun clearLoginError() {
        _loginUiState.update { it.copy(error = null) }
    }

    fun clearRegisterError() {
        _registerUiState.update { it.copy(error = null) }
    }

    fun clearResetPasswordError() {
        _resetPasswordUiState.update { it.copy(error = null) }
    }

    fun resetAuthState() {
        accountRepository.setAuthenticated()
    }

    fun checkUserHasWordbook(any: (Boolean)->Unit) {
        viewModelScope.launch {
            try {
                val profile = userRepository.getProfile()
                val hasWordbook = profile.wordbook?.id != null
                any.invoke(hasWordbook)
            } catch (e: Exception) {
                e.printStackTrace()
                any.invoke(false)
            }
        }
    }
}