package com.avrioc.cleangallery.di

import android.content.ContentResolver
import android.content.Context
import com.avrioc.cleangallery.data.repository.MediaRepositoryImpl
import com.avrioc.cleangallery.domain.repository.IMediaRepository
import com.avrioc.cleangallery.presentation.utils.PermissionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver {
        return context.contentResolver
    }

    @Provides
    @Singleton
    fun providePermissionManager(@ApplicationContext context: Context): PermissionManager {
        return PermissionManager(context)
    }

    @Provides
    @Singleton
    fun provideMediaRepository(contentResolver: ContentResolver): IMediaRepository {
        return MediaRepositoryImpl(contentResolver)
    }


}