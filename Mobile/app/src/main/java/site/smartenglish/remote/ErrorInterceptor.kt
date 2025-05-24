package site.smartenglish.remote

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import site.smartenglish.data.DataStoreManager
import javax.inject.Inject

class ErrorInterceptor@Inject constructor(
    private val dataStoreManager: DataStoreManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code == 401) {
            // TODO 重定向到登录页面
            runBlocking {
                dataStoreManager.clearToken()
            }
            throw Exception("Unauthorized access - please log in again.")
        }
        return response
    }
}