package site.smartenglish.remote.data



data class RegisterRequest(
    val phone: String,
    val verification: String? = null,
    val password: String? = null
)

data class RegisterResponse(
    val message:String
)

data class LoginRequest(
    val phone: String,
    val verification: String? = null,
    val password: String? = null
)

data class LoginResponse(
    val message: String
)

data class ChangePasswordRequest(
    val phone: String,
    val verification: String? = null,
    val password: String? = null
)

data class ChangePasswordResponse(
    val message: String
)