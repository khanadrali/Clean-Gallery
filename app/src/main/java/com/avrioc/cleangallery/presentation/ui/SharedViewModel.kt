package com.avrioc.cleangallery.presentation.ui

import androidx.lifecycle.ViewModel
import com.avrioc.cleangallery.domain.model.Media
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {

    private val _mediaList = MutableStateFlow<List<Media>>(emptyList())
    val mediaList: StateFlow<List<Media>> = _mediaList.asStateFlow()

    private val _loadMedia = MutableSharedFlow<Unit>(replay = 1)
    val loadMedia: SharedFlow<Unit> = _loadMedia.asSharedFlow()

    var albumName = EMPTY_STRING
    var singleImage: Media? = null

    fun loadMedia() {
        _loadMedia.tryEmit(Unit)
    }

    fun updateMediaList(mediaList: List<Media>) {
        _mediaList.value = mediaList
    }

    companion object {
        private const val EMPTY_STRING = ""
    }

}