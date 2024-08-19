package com.avrioc.cleangallery.presentation.ui.album

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avrioc.cleangallery.presentation.adapter.AlbumAdapter
import com.avrioc.cleangallery.domain.model.Album
import com.avrioc.cleangallery.domain.model.Media
import com.avrioc.cleangallery.domain.usecase.GetAllMediaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AlbumListViewModel @Inject constructor(private val useCase: GetAllMediaUseCase) :
    ViewModel() {

    val mediaList = MutableLiveData<List<Media>>()
    val albumList = MutableLiveData<MutableList<Album>>()
    val viewType = MutableLiveData(AlbumAdapter.VIEW_TYPE_GRID)
    val isLoading = MutableLiveData(true)

    fun loadMedia() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                useCase.execute().collect { media ->
                    val images = media.filter { it.isImage() }
                    val videos = media.filter { it.isVideo() }
                    mediaList.postValue(media)
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
                    isLoading.postValue(false)
                    albumList.postValue(albums)
                }
            }
        }
    }

    companion object {
        const val ALL_IMAGES = "All Images"
        const val ALL_VIDEOS = "All Videos"
    }

}