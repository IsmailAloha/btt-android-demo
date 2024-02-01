package com.bluetriangle.bluetriangledemo.utils

import com.bluetriangle.bluetriangledemo.BuildConfig
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class ErrorHandler {


    private val _alertDialogState = MutableStateFlow<AlertDialogState?>(null)
    val alertDialogState: StateFlow<AlertDialogState?>
        get() = _alertDialogState


    var alertView: AlertView? = null

    suspend fun showError(e: Exception) {
        val errorState = AlertDialogState("Error", e.message ?: "Unknown")
        if(BuildConfig.FLAVOR == "compose") {
            _alertDialogState.value = errorState
        } else {
            withContext(Main) {
                alertView?.showAlert(errorState)
            }
        }
    }

    fun onAlertOkClick() {
        _alertDialogState.value = null
    }
}