package com.avrioc.cleangallery.presentation.ui.singlephoto

import androidx.lifecycle.ViewModel
import com.avrioc.cleangallery.domain.model.Media
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class SinglePhotoViewModel : ViewModel() {

    private val _singleImage = MutableStateFlow<Media?>(null)
    val singleImage: StateFlow<Media?> = _singleImage.asStateFlow()

    private val _navigateBack = MutableSharedFlow<Unit>(replay = 1)
    val navigateBack: SharedFlow<Unit> = _navigateBack.asSharedFlow()

    fun onBackPressed() {
        _navigateBack.tryEmit(Unit)
    }

    fun updateImage(singleImage: Media?) {
        _singleImage.value = singleImage
    }
}