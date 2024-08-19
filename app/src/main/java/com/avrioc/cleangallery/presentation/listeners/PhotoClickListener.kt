package com.example.testgallaryapplication.presentation.listeners

import com.avrioc.cleangallery.domain.model.Media

interface PhotoClickListener {
    fun onPhotoClick(media: Media)
}