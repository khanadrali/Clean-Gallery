package com.avrioc.cleangallery.presentation.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.avrioc.cleangallery.R
import com.avrioc.cleangallery.databinding.ActivityMainBinding
import com.avrioc.cleangallery.presentation.utils.PermissionDialogHelper
import com.avrioc.cleangallery.presentation.utils.PermissionManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: SharedViewModel by viewModels()

    @Inject
    lateinit var permissionManager: PermissionManager
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val permissionDialogHelper by lazy {
        PermissionDialogHelper(this,
            onDeleteConfirmed = { handleDeleteConfirmed() },
            onCancel = { handleCancel() })
    }

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            permissionManager.handlePermissionsResult(results, onPartialPermissionsGranted = {
                viewModel.loadMedia(true)
            }, onFullPermissionsGranted = { viewModel.loadMedia(true) }, onPermissionsDenied = {
                permissionDialogHelper.show()
            })
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    override fun onStart() {
        super.onStart()
        checkStoragePermission()
    }


    private fun initView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment?
                ?: return
        navController = navHostFragment.navController
    }

    private fun checkStoragePermission() {
        permissionManager.setPermissionLauncher(requestPermissionsLauncher)
        permissionManager.checkAndRequestPermissions(onPartialPermissionsGranted = {
            viewModel.loadMedia(
                true
            )
        },
            onFullPermissionsGranted = { viewModel.loadMedia(true) },
            onPermissionsDenied = {
                permissionDialogHelper.show()
            })
    }

    private fun handleCancel() {
        permissionDialogHelper.dismiss()
        finish()
    }

    private fun handleDeleteConfirmed() {
        permissionDialogHelper.dismiss()
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.parse("package:${packageName}")
        }
        startActivity(intent)
    }
}