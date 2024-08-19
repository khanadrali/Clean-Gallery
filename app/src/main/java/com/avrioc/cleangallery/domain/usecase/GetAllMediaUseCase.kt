package com.avrioc.cleangallery.domain.usecase

import com.avrioc.cleangallery.domain.repository.IMediaRepository

import com.avrioc.cleangallery.domain.model.Media
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllMediaUseCase @Inject constructor(
    private val mediaRepository: IMediaRepository
) {
    suspend fun execute(): Flow<List<Media>> {
        return mediaRepository.getAllMedia()
    }
}