package site.smartenglish.util

import org.json.JSONObject

/**
 * 处理Retrofit响应结果的扩展函数
 *
 * 此函数检查API响应是否成功。如果成功，返回true；
 * 如果失败，则提取错误信息并抛出异常。
 *
 * @param errorPrefix 错误消息的前缀，用于指明发生错误的操作类型
 * @return 如果响应成功则返回true
 * @throws Exception 当响应失败时，抛出包含错误详情的异常
 */
fun retrofit2.Response<*>.handleResponse(errorPrefix: String) : Boolean{
    if (isSuccessful) {
        return true
    } else {
        handleErrorResponse(this, errorPrefix)
    }
}

private fun handleErrorResponse(response: retrofit2.Response<*>, errorPrefix: String): Nothing {
    val errorBody = response.errorBody()?.string()
    val errorJson = JSONObject(errorBody ?: "{}")
    val errorMessage = errorJson.optString("message", "未知错误")
    throw Exception("$errorPrefix: $errorMessage")
}