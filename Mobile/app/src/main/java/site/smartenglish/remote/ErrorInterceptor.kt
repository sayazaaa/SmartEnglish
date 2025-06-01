package site.smartenglish.remote

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import site.smartenglish.manager.DataStoreManager
import site.smartenglish.manager.SessionManager
import site.smartenglish.repository.AccountRepository
import javax.inject.Inject

class ErrorInterceptor @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val sessionManager: SessionManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code == 401) {
            // 设置未授权状态 触发重定向
            sessionManager.setUnauthorized()
            runBlocking {
                dataStoreManager.clearToken()
            }
            throw Exception("登录已过期，请重新登录")
        }
        return response
    }
}