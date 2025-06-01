package site.smartenglish.manager

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {
    private val _authState = MutableStateFlow(AuthState.AUTHENTICATED)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun setUnauthorized() {
        _authState.value = AuthState.UNAUTHORIZED
    }

    fun setAuthenticated() {
        _authState.value = AuthState.AUTHENTICATED
    }

    enum class AuthState {
        AUTHENTICATED,
        UNAUTHORIZED
    }
}