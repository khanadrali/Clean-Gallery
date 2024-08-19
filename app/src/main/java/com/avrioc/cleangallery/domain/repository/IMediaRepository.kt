package com.avrioc.cleangallery.domain.repository

import com.avrioc.cleangallery.domain.model.Media
import kotlinx.coroutines.flow.Flow

interface IMediaRepository {

    /**
     * Fetches all media items.
     *
     * @return A [Flow] emitting the complete list of media items.
     */
    suspend fun getAllMedia(): Flow<List<Media>>

}