package com.nutron.imageviewer.module.di

import android.content.Context
import com.nutron.imageviewer.data.datasource.ImageDataSource
import com.nutron.imageviewer.data.datasource.ImageStoreDataSource
import com.nutron.imageviewer.data.datasource.local.ImageLocalDataSource
import com.nutron.imageviewer.data.datasource.local.RealmHelper
import com.nutron.imageviewer.data.datasource.local.RealmHelperImpl
import com.nutron.imageviewer.data.datasource.local.SharePrefDataSource
import com.nutron.imageviewer.data.datasource.local.SharePrefDataSourceImpl
import com.nutron.imageviewer.data.datasource.remote.AuthConfigProvider
import com.nutron.imageviewer.data.datasource.remote.ImageApi
import com.nutron.imageviewer.data.datasource.remote.ImageRemoteDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Named


@Module
class DataSourceModule {

    @Provides
    @AppScope
    fun providePreferenceDataSource(context: Context): SharePrefDataSource = SharePrefDataSourceImpl(context)

    @Provides
    @AppScope
    fun provideRealmHelper(): RealmHelper = RealmHelperImpl()

    @Provides
    @AppScope
    @Named(DI_NAME_VALUE_LOCAL_DATA_SOURCE)
    fun provideImageLocalDataSource(
        realmHelper: RealmHelper
    ): ImageDataSource = ImageLocalDataSource(realmHelper)

    @Provides
    @AppScope
    fun provideImageStoreDataSource(
        @Named(DI_NAME_VALUE_LOCAL_DATA_SOURCE) local: ImageDataSource
    ): ImageStoreDataSource = local as ImageStoreDataSource

    @Provides
    @AppScope
    @Named(DI_NAME_VALUE_REMOTE_DATA_SOURCE)
    fun provideImageRemoteDataSource(
        imageApi: ImageApi,
        authConfigProvider: AuthConfigProvider
    ): ImageDataSource = ImageRemoteDataSource(imageApi, authConfigProvider)

}