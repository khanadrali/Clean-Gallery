package com.avrioc.cleangallery.presentation.ui.album

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avrioc.cleangallery.presentation.adapter.AlbumAdapter
import com.avrioc.cleangallery.domain.model.Album
import com.avrioc.cleangallery.domain.model.Media
import com.avrioc.cleangallery.domain.usecase.GetAllMediaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AlbumListViewModel @Inject constructor(private val useCase: GetAllMediaUseCase) :
    ViewModel() {

    private val _mediaList = MutableStateFlow<List<Media>>(emptyList())
    val mediaList: StateFlow<List<Media>> = _mediaList.asStateFlow()

    private val _albumList = MutableStateFlow<MutableList<Album>>(mutableListOf())
    val albumList: StateFlow<MutableList<Album>> = _albumList.asStateFlow()

    private val _viewType = MutableStateFlow(AlbumAdapter.VIEW_TYPE_GRID)
    val viewType: StateFlow<Int> = _viewType.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _forceUpdateTrigger = MutableStateFlow(0)

    val isAlbumListEmpty: StateFlow<Boolean> = combine(
        _albumList,
        _forceUpdateTrigger
    ) { list, _ -> list.isEmpty() }
        .stateIn(viewModelScope, SharingStarted.Lazily, true)

    fun loadMedia() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                useCase.execute().collect { media ->
                    val images = media.filter { it.isImage() }
                    val videos = media.filter { it.isVideo() }
                    _mediaList.emit(media)
                    val albums = media.groupBy { it.bucketDisplayName }.map {
                        Album(
                            it.key,
                            it.value.size,
                            it.value.first().uri,
                            it.value.first().isVideo(),
                            it.value.first().dateAdded
                        )
                    }.toMutableList()

                    // Add an album for all images
                    if (images.isNotEmpty()) {
                        albums.add(
                            0, Album(
                                ALL_IMAGES,
                                images.size,
                                images.first().uri,
                                false,
                                images.first().dateAdded,
                            )
                        )
                    }

                    // Add an album for all videos
                    if (videos.isNotEmpty()) {
                        albums.add(
                            1, Album(
                                ALL_VIDEOS,
                                videos.size,
                                videos.first().uri,
                                true,
                                images.first().dateAdded,
                            )
                        )
                    }
                    _isLoading.emit(false)
                    _albumList.emit(albums) // Emit new albums
                    _forceUpdateTrigger.value++
                }
            }
        }
    }

    fun updateViewType(viewType: Int) {
        _viewType.value = viewType
    }

    fun updateAlbumList(sortedList: MutableList<Album>?) {
        _albumList.value = sortedList ?: mutableListOf()
    }

    companion object {
        const val ALL_IMAGES = "All Images"
        const val ALL_VIDEOS = "All Videos"
    }

}