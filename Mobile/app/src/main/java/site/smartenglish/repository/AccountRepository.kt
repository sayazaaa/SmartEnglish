package site.smartenglish.repository

import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject
import site.smartenglish.manager.DataStoreManager
import site.smartenglish.manager.SessionManager
import site.smartenglish.remote.ApiService
import site.smartenglish.remote.data.ChangePasswordRequest
import site.smartenglish.remote.data.LoginRequest
import site.smartenglish.remote.data.RegisterRequest
import site.smartenglish.util.handleResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    private val api: ApiService,
    private val dataStoreManager: DataStoreManager,
    private val sessionManager: SessionManager,
) {
    sealed class Credentials {
        data class Password(val phone: String, val password: String) : Credentials()
        data class Verification(val phone: String, val verification: String) : Credentials()
    }

    private suspend fun login(credentials: Credentials): Boolean {
        val response = when (credentials) {
            is Credentials.Password -> api.login(
                LoginRequest(
                    phone = credentials.phone, password = credentials.password
                )
            )

            is Credentials.Verification -> api.login(
                LoginRequest(
                    phone = credentials.phone, verification = credentials.verification
                )
            )
        }
        if (response.isSuccessful) {
            val token = response.headers()["Authorization"]

            token?.let {
                dataStoreManager.saveToken(it)
            }
            return true
        } else {
            // 从错误响应中提取信息
            val errorBody = response.errorBody()?.string()
            val errorJson = JSONObject(errorBody ?: "{}")
            val errorMessage = errorJson.optString("message", "未知错误")
            throw Exception("登录失败: $errorMessage")
        }
    }

    suspend fun loginWithPassword(phone: String, password: String): Boolean {
        return login(Credentials.Password(phone, password))
    }

    suspend fun loginWithVerification(phone: String, verification: String): Boolean {
        return login(Credentials.Verification(phone, verification))
    }

    suspend fun register(phone: String, verification: String, password: String): Boolean {
        return api.register(RegisterRequest(phone, verification, password))
            .handleResponse("注册失败").let { it == null }
    }


    suspend fun getVerificationCode(phone: String): Boolean {
        return api.register(RegisterRequest(phone)).handleResponse("获取验证码失败")
            .let { it == null }
    }

    suspend fun resetPassword(phone: String, verification: String, newPassword: String): Boolean {
        return api.changePassword(
            ChangePasswordRequest(
                phone, verification, newPassword
            )
        ).handleResponse("重置密码失败").let { it == null }
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
