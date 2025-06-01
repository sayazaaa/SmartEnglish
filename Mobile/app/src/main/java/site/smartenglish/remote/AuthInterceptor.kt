package site.smartenglish.remote

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import site.smartenglish.manager.DataStoreManager
import javax.inject.Inject

/**
 * 一个 OkHttp 拦截器，为每个请求添加令牌的 Authorization 头。
 *
 * @param dataStoreManager 用于获取存储的令牌。
 */
class AuthInterceptor @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        val token = dataStoreManager.getTokenSync()
        val request = if (!token.isNullOrEmpty()) {
            Log.d("AuthInterceptor", "Token found, adding Authorization header")
            requestBuilder
                .addHeader("Authorization", "$token")
                .build()
        } else {
            Log.d("AuthInterceptor", "No token found, proceeding without Authorization header")
            requestBuilder.build()
        }

        return chain.proceed(request)
    }
}
