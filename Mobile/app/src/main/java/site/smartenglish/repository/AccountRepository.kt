package site.smartenglish.repository

import javax.inject.Inject
import javax.inject.Singleton
import site.smartenglish.data.DataStoreManager
import site.smartenglish.remote.ApiService
import site.smartenglish.remote.data.LoginRequest

@Singleton
class AccountRepository @Inject constructor(
    private val api: ApiService,
    private val dataStoreManager: DataStoreManager
) {
    suspend fun login(phone: String,verification:String, password: String) {
        val response = api.login(LoginRequest(phone, verification, password))
        if (response.isSuccessful) {
            val token = response.headers()["Authorization"]
            val tokenValue = token?.substring(7)

            tokenValue?.let {
                dataStoreManager.saveToken(it)
            }
        }
        else {
            throw Exception("Login failed: ${response.errorBody()?.string()}")
        }
    }

}
