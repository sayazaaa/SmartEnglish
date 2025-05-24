package site.smartenglish.remote

import okhttp3.Interceptor
import okhttp3.Response
import site.smartenglish.data.DataStoreManager
import javax.inject.Inject

/**
 * 一个 OkHttp 拦截器，为每个请求添加带有 Bearer 令牌的 Authorization 头。
 *
 * @param tokenProvider 提供要添加到请求头中的令牌的 lambda 函数。
 */
class AuthInterceptor@Inject constructor(
    private val dataStoreManager: DataStoreManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        val token = dataStoreManager.getTokenSync()
        val request = if (!token.isNullOrEmpty()) {
            requestBuilder
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            requestBuilder.build()
        }

        return chain.proceed(request)
    }
}
