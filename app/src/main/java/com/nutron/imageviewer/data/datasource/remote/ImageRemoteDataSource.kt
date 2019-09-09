package com.nutron.imageviewer.data.datasource.remote

import com.nutron.imageviewer.data.datasource.ImageDataSource
import com.nutron.imageviewer.data.entity.ImageData
import io.reactivex.Observable


class ImageRemoteDataSource(
    val imageApi: ImageApi,
    val authConfigProvider: AuthConfigProvider
): ImageDataSource {

    override fun getImages(): Observable<List<ImageData>> {
        return imageApi.getImages(authConfigProvider.getAccessKey())
    }
}