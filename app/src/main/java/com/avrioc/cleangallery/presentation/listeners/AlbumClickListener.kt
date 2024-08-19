package com.example.testgallaryapplication.presentation.listeners

import com.avrioc.cleangallery.domain.model.Album

interface AlbumClickListener {
    fun onAlbumClick(album: Album)
}