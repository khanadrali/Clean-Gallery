package com.avrioc.cleangallery.presentation.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {

    val loadMedia = MutableLiveData<Boolean>()

    fun loadMedia(shouldLoadMedia: Boolean) {
        loadMedia.value = shouldLoadMedia
    }


}