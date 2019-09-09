package com.nutron.imageviewer.data.datasource.remote

import com.nutron.imageviewer.data.entity.ImageData
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface ImageApi {

    @GET("/photos")
    fun getImages(@Query("client_id") accessKey: String): Observable<List<ImageData>>
}