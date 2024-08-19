package com.avrioc.cleangallery.domain.model

import android.net.Uri

data class Media(
    val uri: Uri,
    val name: String,
    val bucketId: Int,
    val bucketDisplayName: String,
    val data: String,
    val mimeType: String,
    val dateAdded: Long
){

    fun isVideo(): Boolean = mimeType.startsWith("video/")

    fun isImage(): Boolean = mimeType.startsWith("image/")
}
