package com.tiizzer.narz.pandasoft.challenge.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.tiizzer.narz.pandasoft.challenge.R

object DialogHelper {
    fun showSessionExpireDialog(context: Context, action: () -> Unit) {
        AlertDialog.Builder(context).apply {
            setTitle(context.getString(R.string.session_dialog_title))
            setMessage(context.getString(R.string.session_dialog_message))
            setCancelable(false)
            setPositiveButton(context.getString(R.string.app_ok)) { dialog, which ->
                action.invoke()
                dialog.dismiss()
            }
        }.create().show()
    }
}