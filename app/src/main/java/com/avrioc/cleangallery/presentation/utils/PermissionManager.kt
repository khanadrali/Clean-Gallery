package com.avrioc.cleangallery.presentation.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

// This class is responsible for managing permissions in the app for all the Android Version
// including Partial permission handling for Android 13 (Tiramisu) as well.
class PermissionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var onPartialPermissionsGranted: (() -> Unit)? = null
    private var onPermissionsDenied: (() -> Unit)? = null

    fun setPermissionLauncher(launcher: ActivityResultLauncher<Array<String>>) {
        permissionLauncher = launcher
    }

    fun checkAndRequestPermissions(
        onFullPermissionsGranted: () -> Unit,
        onPartialPermissionsGranted: (() -> Unit)? = null,
        onPermissionsDenied: () -> Unit
    ) {
        val permissions = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
            )

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO
            )

            else -> arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        val deniedPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (deniedPermissions.isEmpty()) {
            // All requested permissions are granted
            onFullPermissionsGranted()
        } else {
            // Request the denied permissions
            permissionLauncher.launch(deniedPermissions)
            // Store callback for partial permission handling
            this.onPartialPermissionsGranted = onPartialPermissionsGranted
            this.onPermissionsDenied = onPermissionsDenied
        }
    }

    fun handlePermissionsResult(
        results: Map<String, Boolean>,
        onFullPermissionsGranted: () -> Unit,
        onPartialPermissionsGranted: (() -> Unit)? = null,
        onPermissionsDenied: () -> Unit
    ) {
        val allGranted = results.all { (permission, granted) ->
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }

        if (allGranted) {
            onFullPermissionsGranted()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                val partiallyGranted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                ) == PackageManager.PERMISSION_GRANTED
                if (partiallyGranted) {
                    onPartialPermissionsGranted?.invoke()
                    return
                }
            }

            onPermissionsDenied()

        }
    }
}