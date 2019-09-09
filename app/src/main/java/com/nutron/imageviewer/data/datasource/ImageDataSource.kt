package com.nutron.imageviewer.data.datasource

import com.nutron.imageviewer.data.entity.ImageData
import io.reactivex.Completable
import io.reactivex.Observable


interface ImageDataSource {
    fun getImages(): Observable<List<ImageData>>

}

interface ImageStoreDataSource {
    fun saveImages(data: List<ImageData>): Completable
}

