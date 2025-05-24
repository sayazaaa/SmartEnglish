package site.smartenglish.remote

import retrofit2.http.Body
import retrofit2.http.GET
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
    ): RegisterResponse

    @GET("account")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @PUT("account")
    suspend fun changePassword(
        @Body changePasswordRequest: ChangePasswordRequest
    ): ChangePasswordResponse



}
