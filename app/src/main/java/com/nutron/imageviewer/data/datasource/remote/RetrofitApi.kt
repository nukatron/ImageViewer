package com.nutron.imageviewer.data.datasource.remote

import com.nutron.imageviewer.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitApi {

    fun getApi(): ImageApi {
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .readTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
                    .apply {
                        if (BuildConfig.DEBUG) {
                            val logging = HttpLoggingInterceptor()
                            logging.level = HttpLoggingInterceptor.Level.BODY
                            addInterceptor(logging)
                        }
                    }.build()
            )
            .baseUrl(BASE_URL)
            .build()
            .create(ImageApi::class.java)
    }
}