package com.nutron.imageviewer.data.datasource.local

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nutron.imageviewer.data.datasource.ImageDataSource
import com.nutron.imageviewer.data.datasource.ImageStoreDataSource
import com.nutron.imageviewer.data.entity.ImageData
import io.reactivex.Completable
import io.reactivex.Observable


class ImageLocalDataSource(
    val pref: SharePrefDataSource,
    val gson: Gson
    ): ImageDataSource, ImageStoreDataSource {

    private val IMAGE_PREF_KEY = "image_pref_key"

    override fun getImages(): Observable<List<ImageData>> {
        val jsonData = pref.getValue<String?>(IMAGE_PREF_KEY, null)
        val turnsType = object : TypeToken<List<ImageData>>() {}.type
        val data = gson.fromJson<List<ImageData>>(jsonData, turnsType)
        return Observable.just(data ?: arrayListOf())
    }

    override fun saveImages(data: List<ImageData>): Completable {
        return Completable.fromAction {
            pref.save(IMAGE_PREF_KEY, gson.toJson(data))
        }
    }
}