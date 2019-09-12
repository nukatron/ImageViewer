package com.nutron.imageviewer.data.datasource.local

import com.nutron.imageviewer.data.datasource.ImageDataSource
import com.nutron.imageviewer.data.datasource.ImageStoreDataSource
import com.nutron.imageviewer.data.entity.ImageData
import com.nutron.imageviewer.data.entity.ImageDataRealm
import io.reactivex.Completable
import io.reactivex.Observable
import io.realm.Realm


class ImageLocalDataSource: ImageDataSource, ImageStoreDataSource {


    /**
     * Note: For the localDataSource, I design for making it flexible to change the way to keep the data later.
     * We change change to use Room, Realm or any other tools for storing data without any effect to the presentation layout
     */
    override fun getImages(): Observable<List<ImageData>> {
        val realm = Realm.getDefaultInstance()
        return realm.use {
            val result = realm.where(ImageDataRealm::class.java).findAll()
            val list = realm.copyFromRealm(result).map { it.realmToData() }
            list.takeIf { it.isNotEmpty() }?.let { Observable.just(it) } ?: Observable.empty()
        }
    }

    override fun saveImages(data: List<ImageData>): Completable {
        return Completable.fromAction {
            val realm = Realm.getDefaultInstance()
            realm.use {
                realm.beginTransaction()
                for (image in data) {
                    val imageRealm = ImageDataRealm().dataToRealm(image)
                    realm.copyToRealmOrUpdate(imageRealm)
                }
                realm.commitTransaction()
            }
        }
    }
}