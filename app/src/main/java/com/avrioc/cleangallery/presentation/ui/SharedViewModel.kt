package com.avrioc.cleangallery.presentation.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.avrioc.cleangallery.domain.model.Media
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {


    val mediaList = MutableLiveData<List<Media>>()
    val loadMedia = MutableLiveData<Boolean>()
    var albumName = EMPTY_STRING
    var singleImage: Media?=null


    fun loadMedia(shouldLoadMedia: Boolean) {
        loadMedia.value = shouldLoadMedia
    }

    companion object {
        private const val EMPTY_STRING = ""
    }

}