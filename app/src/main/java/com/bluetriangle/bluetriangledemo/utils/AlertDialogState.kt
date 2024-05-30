package com.bluetriangle.bluetriangledemo.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog

class AlertDialogState(val title: String, val message: String)

interface AlertView {
    fun showAlert(alertDialogState: AlertDialogState)
}

fun Context.showAlert(alertDialogState: AlertDialogState) {
    AlertDialog.Builder(this)
        .setTitle(alertDialogState.title)
        .setMessage(alertDialogState.message)
        .setPositiveButton("Ok") { dialog,_ ->
            dialog.dismiss()
        }.show()
}