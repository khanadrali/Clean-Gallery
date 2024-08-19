package com.avrioc.cleangallery.domain.model

import android.net.Uri

data class Album(
    val albumName: String,
    val mediaCount: Int,
    val thumbnailUrl: Uri?,
    val isVideo: Boolean,
    val dateAdded: Long?,
)