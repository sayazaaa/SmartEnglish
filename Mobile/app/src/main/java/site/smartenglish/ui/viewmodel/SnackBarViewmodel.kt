package site.smartenglish.ui.viewmodel

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SnackBarViewmodel @Inject constructor() : ViewModel() {
    val snackbarHostState = SnackbarHostState()

    fun showSnackbar(
        message: String,
        actionLabel: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short,
        withDismissAction: Boolean = false,
        onAction: (() -> Unit)? = null,
        onDismiss: (() -> Unit)? = null
    ) {
        viewModelScope.launch{
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                duration = duration,
                withDismissAction = withDismissAction
            )
            when (result) {
                SnackbarResult.ActionPerformed -> {
                    onAction?.invoke()
                    onDismiss?.invoke()
                }

                SnackbarResult.Dismissed -> {
                    onDismiss?.invoke()
                }
            }
        }
    }
}