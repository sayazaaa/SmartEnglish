package site.smartenglish.remote

import android.util.Log
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
import site.smartenglish.manager.DataStoreManager
import site.smartenglish.manager.SessionManager
import javax.inject.Inject

class ErrorInterceptor @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val sessionManager: SessionManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.code == 401) {
            // 设置未授权状态 触发重定向
            sessionManager.setUnauthorized()
            runBlocking {
                dataStoreManager.clearToken()
            }

            // 创建一个包含错误信息的响应体
            val errorMessage = "登录已过期，请重新登录"
            val jsonObject = JSONObject().apply {
                put("message", errorMessage)
                put("code", 401)
            }

            // 返回一个新的响应，而不是抛出异常
            return createErrorResponse(response, jsonObject.toString(), 401)
        }

        if (response.code != 200) {
            Log.e("ErrorInterceptor", "Received error response: ${response.code}")

            try {
                val responseBody = response.peekBody(Long.MAX_VALUE)
                val errorBody = responseBody.string()

                // 尝试解析JSON错误信息
                val errorJson = try {
                    JSONObject(errorBody)
                } catch (e: Exception) {
                    // JSON解析失败，创建新的JSON对象
                    JSONObject()
                }

                // 如果没有message字段或为空，添加"未知错误"
                if (!errorJson.has("message") || errorJson.getString("message").isEmpty()) {
                    errorJson.put("message", "未知错误")
                }

                // 添加状态码
                errorJson.put("code", response.code)

                // 返回自定义错误响应
                return createErrorResponse(response, errorJson.toString(), response.code)
            } catch (e: Exception) {
                Log.e("ErrorInterceptor", "Error handling response", e)

                // 处理异常，创建包含"未知错误"的响应
                val jsonObject = JSONObject().apply {
                    put("message", "未知错误")
                    put("code", response.code)
                }

                return createErrorResponse(response, jsonObject.toString(), response.code)
            }
        }

        return response
    }

    private fun createErrorResponse(response: Response, errorBody: String, code: Int): Response {
        return Response.Builder()
            .request(response.request)
            .protocol(Protocol.HTTP_1_1)
            .code(code)
            .message(if (code == 401) "Unauthorized" else "Error")
            .body(errorBody.toResponseBody("application/json".toMediaTypeOrNull()))
            .headers(response.headers)
            .build()
    }
}