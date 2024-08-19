package com.avrioc.cleangallery.presentation.ui.singlephoto

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.avrioc.cleangallery.domain.model.Media

class SinglePhotoViewModel : ViewModel() {

    val singleImage = MutableLiveData<Media>()
    val navigateBack = MutableLiveData<Boolean>()
    fun onBackPressed() {
        navigateBack.value = true
    }
}