package com.nutron.imageviewer.di

import com.google.gson.Gson
import com.nutron.imageviewer.data.datasource.remote.AuthConfigProvider
import com.nutron.imageviewer.data.datasource.remote.AuthConfigProviderImpl
import com.nutron.imageviewer.data.datasource.remote.ImageApi
import com.nutron.imageviewer.data.datasource.remote.RetrofitApi
import dagger.Module
import dagger.Provides


@Module
class NetworkModule {

    @AppScope
    @Provides
    fun provideGson(): Gson = Gson()

    @AppScope
    @Provides
    fun provideAuthConfigProvider(): AuthConfigProvider = AuthConfigProviderImpl()

    @AppScope
    @Provides
    fun provideImageApi(): ImageApi {
        return RetrofitApi().getApi()
    }
}