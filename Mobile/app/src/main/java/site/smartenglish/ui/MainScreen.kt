package site.smartenglish.ui

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import site.smartenglish.ui.viewmodel.SnackBarViewmodel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    snackBarViewmodel: SnackBarViewmodel = hiltViewModel()
) {

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarViewmodel.snackbarHostState
            )
        }) {
        MainNav()
    }

}