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

    /**
     * Note: For the localDataSource, I design for making it flexible to change the way to keep the data later.
     * We change change to use Room, Realm or any other tools for storing data without any effect to the presentation layout
     * In this case, since data is only 10 items, I prefer to use SharePref fist to reduce implementation time.
     */
    override fun getImages(): Observable<List<ImageData>> {
        val jsonData = pref.getValue<String?>(IMAGE_PREF_KEY, null)
        val turnsType = object : TypeToken<List<ImageData>>() {}.type
        val data = gson.fromJson<List<ImageData>>(jsonData, turnsType)
        return data?.let { Observable.just(it) } ?: Observable.empty()
    }

    override fun saveImages(data: List<ImageData>): Completable {
        return Completable.fromAction {
            pref.save(IMAGE_PREF_KEY, gson.toJson(data))
        }
    }
}