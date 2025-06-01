package site.smartenglish.ui.viewmodel

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SnackBarViewmodel @Inject constructor() : ViewModel() {

    val snackbarHostState = SnackbarHostState()

    fun showSnackbar(message: String, actionLabel: String? = null) {
        viewModelScope.launch{
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                duration = SnackbarDuration.Short
            )
        }
    }
}