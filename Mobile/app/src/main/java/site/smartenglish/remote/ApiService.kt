package site.smartenglish.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import site.smartenglish.remote.data.ChangePasswordRequest
import site.smartenglish.remote.data.ChangePasswordResponse
import site.smartenglish.remote.data.LoginRequest
import site.smartenglish.remote.data.LoginResponse
import site.smartenglish.remote.data.RegisterRequest
import site.smartenglish.remote.data.RegisterResponse


interface ApiService {

    @POST("account")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): Response<RegisterResponse>

    @POST("account/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @PUT("account")
    suspend fun changePassword(
        @Body changePasswordRequest: ChangePasswordRequest
    ): Response<ChangePasswordResponse>



}
