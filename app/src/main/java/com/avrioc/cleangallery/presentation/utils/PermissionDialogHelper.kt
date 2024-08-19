package com.avrioc.cleangallery.presentation.utils

import android.app.Activity
import com.avrioc.cleangallery.R
import dev.shreyaspatil.MaterialDialog.MaterialDialog

class PermissionDialogHelper(
    private val context: Activity,
    private val onDeleteConfirmed: () -> Unit,
    private val onCancel: () -> Unit
) {

    private val dialog by lazy {
        MaterialDialog.Builder(context)
            .setTitle(context.getString(R.string.title_permission_required))
            .setMessage(context.getString(R.string.permission_required_message))
            .setCancelable(false)
            .setNegativeButton(context.getString(R.string.close_gallery)) { _, _ ->
                onCancel()
            }.setPositiveButton(context.getString(R.string.give_permission)) { _, _ ->
                onDeleteConfirmed()
            }
            .build()
    }

    fun show() {
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }
}