package site.smartenglish.repository

import javax.inject.Inject
import javax.inject.Singleton
import site.smartenglish.data.DataStoreManager
import site.smartenglish.remote.ApiService
import site.smartenglish.remote.data.LoginRequest
import site.smartenglish.remote.data.RegisterRequest

@Singleton
class AccountRepository @Inject constructor(
    private val api: ApiService,
    private val dataStoreManager: DataStoreManager
) {
    suspend fun login(phone: String,verification:String, password: String):Boolean {
        val response = api.login(LoginRequest(phone, verification, password))
        if (response.isSuccessful) {
            val token = response.headers()["Authorization"]

            token?.let {
                dataStoreManager.saveToken(it)
            }
            return true
        }
        else {
            throw Exception("登录失败: ${response.errorBody()?.string()}")
        }
    }

    suspend fun register(phone: String, verification: String, password: String):Boolean {
        val response = api.register(RegisterRequest(phone, verification, password))
        if (response.isSuccessful) {
            return true
        } else {
            throw Exception("注册失败: ${response.errorBody()?.string()}")
        }
    }

    suspend fun getVerificationCode(phone: String):Boolean {
        val response = api.register(RegisterRequest(phone))
        if (!response.isSuccessful) {
            throw Exception("获取验证码失败: ${response.errorBody()?.string()}")
        }
        return true
    }

}
