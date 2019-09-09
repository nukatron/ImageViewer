package com.nutron.imageviewer.di

import com.nutron.imageviewer.data.datasource.ImageDataSource
import com.nutron.imageviewer.data.datasource.ImageRepository
import com.nutron.imageviewer.data.datasource.ImageRepositoryImpl
import com.nutron.imageviewer.data.datasource.ImageStoreDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class AppModule {

    @Provides
    @AppScope
    fun provideImageRepository(
        @Named(DI_NAME_VALUE_LOCAL_DATA_SOURCE) localDataSource: ImageDataSource,
        @Named(DI_NAME_VALUE_REMOTE_DATA_SOURCE) remoteDataSource: ImageDataSource,
        imageStoreDataSource: ImageStoreDataSource
    ): ImageRepository {
        return ImageRepositoryImpl(localDataSource, remoteDataSource, imageStoreDataSource)
    }


}