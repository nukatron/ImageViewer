package com.nutron.imageviewer.module.di

import com.google.gson.GsonBuilder
import com.nutron.imageviewer.BuildConfig
import com.nutron.imageviewer.data.datasource.remote.AuthConfigProvider
import com.nutron.imageviewer.data.datasource.remote.AuthConfigProviderImpl
import com.nutron.imageviewer.data.datasource.remote.BASE_URL
import com.nutron.imageviewer.data.datasource.remote.DATE_FORMAT
import com.nutron.imageviewer.data.datasource.remote.HTTP_TIMEOUT
import com.nutron.imageviewer.data.datasource.remote.ImageApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@Module
class NetworkModule {

    @AppScope
    @Provides
    fun provideAuthConfigProvider(): AuthConfigProvider = AuthConfigProviderImpl()

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
            .apply {
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.level = HttpLoggingInterceptor.Level.BODY
                    addInterceptor(logging)
                }
            }.build()
    }

    @AppScope
    @Provides
    fun provideImageApi(httpClient: OkHttpClient): ImageApi {
        val gson = GsonBuilder().setDateFormat(DATE_FORMAT).create()
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .baseUrl(BASE_URL)
            .build()
            .create(ImageApi::class.java)
    }
}