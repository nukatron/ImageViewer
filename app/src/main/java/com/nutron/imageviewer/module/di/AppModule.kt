package com.nutron.imageviewer.module.di

import android.content.Context
import com.nutron.imageviewer.data.datasource.ImageDataSource
import com.nutron.imageviewer.data.datasource.ImageRepository
import com.nutron.imageviewer.data.datasource.ImageRepositoryImpl
import com.nutron.imageviewer.data.datasource.ImageStoreDataSource
import com.nutron.imageviewer.module.extdi.ImageLoader
import com.nutron.imageviewer.module.extdi.ImageLoaderImpl
import com.nutron.imageviewer.module.extdi.ResourceProvider
import com.nutron.imageviewer.module.extdi.ResourceProviderImpl
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

    @Provides
    @AppScope
    fun provideImageLoader(context: Context): ImageLoader =
        ImageLoaderImpl(context)

    @Provides
    @AppScope
    fun provideResourceProvider(context: Context): ResourceProvider = ResourceProviderImpl(context)


}