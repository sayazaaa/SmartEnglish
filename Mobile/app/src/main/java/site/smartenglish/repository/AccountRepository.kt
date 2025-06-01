package site.smartenglish.repository

import kotlinx.coroutines.flow.StateFlow
import site.smartenglish.manager.DataStoreManager
import site.smartenglish.manager.SessionManager
import site.smartenglish.remote.ApiService
import site.smartenglish.remote.AuthInterceptor
import site.smartenglish.remote.data.LoginRequest
import site.smartenglish.remote.data.RegisterRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    private val api: ApiService,
    private val dataStoreManager: DataStoreManager,
    private val sessionManager: SessionManager,
) {
    suspend fun login(phone: String, verification: String, password: String): Boolean {
        val response = api.login(LoginRequest(phone, verification, password))
        if (response.isSuccessful) {
            val token = response.headers()["Authorization"]

            token?.let {
                dataStoreManager.saveToken(it)
            }
            return true
        } else {
            throw Exception("登录失败: ${response.errorBody()?.string()}")
        }
    }

    suspend fun register(phone: String, verification: String, password: String): Boolean {
        val response = api.register(RegisterRequest(phone, verification, password))
        if (response.isSuccessful) {
            return true
        } else {
            throw Exception("注册失败: ${response.errorBody()?.string()}")
        }
    }

    suspend fun getVerificationCode(phone: String): Boolean {
        val response = api.register(RegisterRequest(phone))
        if (!response.isSuccessful) {
            throw Exception("获取验证码失败: ${response.errorBody()?.string()}")
        }
        return true
    }

    // 暴露认证状态给ViewModel
    val authState: StateFlow<SessionManager.AuthState> = sessionManager.authState

    // 获取token
    fun getToken(): String? = dataStoreManager.getTokenSync()

    // 清除token
    suspend fun clearToken() {
        dataStoreManager.clearToken()
    }

    // 更新认证状态
    fun setAuthenticated() {
        sessionManager.setAuthenticated()
    }

    fun setUnauthorized() {
        sessionManager.setUnauthorized()
    }

    // 检查是否已认证
    fun isAuthenticated(): Boolean =
        sessionManager.authState.value == SessionManager.AuthState.AUTHENTICATED &&
                !dataStoreManager.getTokenSync().isNullOrEmpty()




}
