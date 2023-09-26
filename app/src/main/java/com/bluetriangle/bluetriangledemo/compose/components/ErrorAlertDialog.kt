package com.bluetriangle.bluetriangledemo.compose.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.bluetriangle.bluetriangledemo.utils.ErrorHandler

@Composable
fun ErrorAlertDialog(errorHandler: ErrorHandler) {
    val alertDialogState = errorHandler.alertDialogState.collectAsState()
    if(alertDialogState.value != null) {
        AlertDialog(
            onDismissRequest = {
                errorHandler.onAlertOkClick()
            },
            title = {
                Text(text = alertDialogState.value?.title?:"")
            },
            text = {
                Text(alertDialogState.value?.message?:"")
            },
            confirmButton = {
                Button(
                    onClick = {
                        errorHandler.onAlertOkClick()
                    }) {
                    Text("OK")
                }
            }
        )
    }
}