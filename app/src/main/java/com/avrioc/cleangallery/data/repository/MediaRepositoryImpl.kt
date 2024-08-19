package com.avrioc.cleangallery.data.repository

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore.Images.Media.*
import android.provider.MediaStore.Video
import com.avrioc.cleangallery.domain.model.Media
import com.avrioc.cleangallery.domain.repository.IMediaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(private val contentResolver: ContentResolver):
    IMediaRepository {
    override suspend fun getAllMedia(): Flow<List<Media>> = flow {

        // Projection for querying images
        val imageProjection = arrayOf(
          _ID,
            DISPLAY_NAME,
            BUCKET_ID,
            BUCKET_DISPLAY_NAME,
            DATA,
            MIME_TYPE,
            DATE_ADDED
        )

        // Projection for querying videos
        val videoProjection = arrayOf(
            Video.Media._ID,
            Video.Media.DISPLAY_NAME,
            Video.Media.BUCKET_ID,
            Video.Media.BUCKET_DISPLAY_NAME,
            Video.Media.DATA,
            Video.Media.MIME_TYPE,
            Video.Media.DATE_ADDED
        )

        val mediaList = mutableListOf<Media>()

        // Query images
        val imageCursor = contentResolver.query(
            EXTERNAL_CONTENT_URI,
            imageProjection,
            null,
            null,
            "$DATE_ADDED DESC"
        )

        imageCursor?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(_ID)
            val displayNameColumn = cursor.getColumnIndexOrThrow(DISPLAY_NAME)
            val bucketIdColumn = cursor.getColumnIndexOrThrow(BUCKET_ID)
            val bucketDisplayNameColumn = cursor.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME)
            val dataColumn = cursor.getColumnIndexOrThrow(DATA)
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(MIME_TYPE)
            val dateAddedColumn = cursor.getColumnIndexOrThrow(DATE_ADDED)

            while (cursor.moveToNext()) {
                val uri = Uri.withAppendedPath(
                    EXTERNAL_CONTENT_URI,
                    cursor.getLong(idColumn).toString()
                )
                val name = cursor.getString(displayNameColumn)
                val bucketId = cursor.getInt(bucketIdColumn)
                val bucketDisplayName = cursor.getString(bucketDisplayNameColumn)
                val data = cursor.getString(dataColumn)
                val mimeType = cursor.getString(mimeTypeColumn)
                val dateAdded = cursor.getLong(dateAddedColumn)

                mediaList.add(
                    Media(uri, name, bucketId, bucketDisplayName, data, mimeType,dateAdded)
                )
            }
        }

        // Query videos
        val videoCursor = contentResolver.query(
            Video.Media.EXTERNAL_CONTENT_URI,
            videoProjection,
            null,
            null,
            "${Video.Media.DATE_ADDED} DESC"
        )

        videoCursor?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(Video.Media._ID)
            val displayNameColumn = cursor.getColumnIndexOrThrow(Video.Media.DISPLAY_NAME)
            val bucketIdColumn = cursor.getColumnIndexOrThrow(Video.Media.BUCKET_ID)
            val bucketDisplayNameColumn = cursor.getColumnIndexOrThrow(Video.Media.BUCKET_DISPLAY_NAME)
            val dataColumn = cursor.getColumnIndexOrThrow(Video.Media.DATA)
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(Video.Media.MIME_TYPE)
            val dateAddedColumn = cursor.getColumnIndexOrThrow(Video.Media.DATE_ADDED)

            while (cursor.moveToNext()) {
                val uri = Uri.withAppendedPath(
                    Video.Media.EXTERNAL_CONTENT_URI,
                    cursor.getLong(idColumn).toString()
                )
                val name = cursor.getString(displayNameColumn)
                val bucketId = cursor.getInt(bucketIdColumn)
                val bucketDisplayName = cursor.getString(bucketDisplayNameColumn)
                val data = cursor.getString(dataColumn)
                val mimeType = cursor.getString(mimeTypeColumn)
                val dateAdded = cursor.getLong(dateAddedColumn)

                mediaList.add(
                    Media(uri, name, bucketId, bucketDisplayName, data, mimeType,dateAdded)
                )
            }
        }
        mediaList.sortByDescending { it.dateAdded }
        emit(mediaList)
    }
}