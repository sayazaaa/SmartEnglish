package site.smartenglish.remote.data


data class RegisterRequest(
    val phone: String? = null,
    val verification: String? = null,
    val password: String? = null
)

data class RegisterResponse(
    val message: String? = null
)

data class LoginRequest(
    val phone: String? = null,
    val verification: String? = null,
    val password: String? = null
)

data class LoginResponse(
    val message: String? = null
)

data class ChangePasswordRequest(
    val phone: String? = null,
    val verification: String? = null,
    val password: String? = null
)

data class ChangePasswordResponse(
    val message: String? = null
)