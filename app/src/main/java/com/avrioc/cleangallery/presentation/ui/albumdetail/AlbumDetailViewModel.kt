package com.avrioc.cleangallery.presentation.ui.albumdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.avrioc.cleangallery.domain.model.Media
import com.avrioc.cleangallery.presentation.ui.album.AlbumListViewModel
import com.avrioc.cleangallery.presentation.utils.SingleLiveDataEvent
import javax.inject.Inject

class AlbumDetailViewModel @Inject constructor() : ViewModel() {
    private val _mediaList = MutableLiveData<List<Media>>()
    private val _loading = MutableLiveData<Boolean>()

    private var allMediaList: List<Media> = emptyList()
    private var currentPage = 0

    val mediaList: LiveData<List<Media>> get() = _mediaList
    val loading: LiveData<Boolean> get() = _loading
    val navigateBack = SingleLiveDataEvent<Boolean>()
    val albumName = MutableLiveData<String>()


    fun loadNextPage() {
        if (_loading.value == true) return
        loadPaginatedMedia(currentPage + 1)
    }

    fun onBackPressed() {
        navigateBack.value = true
    }

    fun setMediaList(media: List<Media>) {
        allMediaList = filterAlbumImages(media)
        currentPage = 0 // Reset to the first page
        _loading.value = true // Ensure loading starts
        loadPaginatedMedia(currentPage)
    }

    private fun filterAlbumImages(media: List<Media>) = when (albumName.value) {
        AlbumListViewModel.ALL_IMAGES -> media.filter { it.isImage() }
        AlbumListViewModel.ALL_VIDEOS -> media.filter { it.isVideo() }
        else -> media.filter { it.bucketDisplayName == albumName.value }
    }


    private fun loadPaginatedMedia(page: Int) {

        // if true then No more data to load
        if (page * PAGE_SIZE >= allMediaList.size) {
            _loading.value = false
            return
        }

        val fromIndex = page * PAGE_SIZE
        val toIndex = minOf((page + 1) * PAGE_SIZE, allMediaList.size)
        val paginatedList = allMediaList.subList(fromIndex, toIndex)

        _mediaList.value = paginatedList
        currentPage = page
        _loading.value = false // Set loading to true for the next page
    }

    companion object {
        private const val PAGE_SIZE = 50 // Number of items per page
    }
}