package com.nutron.imageviewer.data.datasource

import com.nutron.imageviewer.data.entity.ImageData
import io.reactivex.Observable


interface ImageRepository {
    fun getImage(): Observable<List<ImageData>>
    fun fetchFromDisk(): Observable<List<ImageData>>
    fun fetchFromRemote(): Observable<List<ImageData>>
}

class ImageRepositoryImpl(
    private val localDataSource: ImageDataSource,
    private val remoteDataSource: ImageDataSource,
    private val storeImageDataSource: ImageStoreDataSource
): ImageRepository {

    override fun getImage(): Observable<List<ImageData>> {
        return Observable.concat(listOf(fetchFromDisk(), fetchFromRemote()))
    }

    override fun fetchFromDisk(): Observable<List<ImageData>> {
        return localDataSource.getImages().map { it.sortedBy { item -> item.createDate } }
    }

    override fun fetchFromRemote(): Observable<List<ImageData>> {
        return remoteDataSource
            .getImages()
            .map { it.sortedBy { item -> item.createDate } }
            .flatMap { data ->
                if(data.isNotEmpty()) {
                    storeImageDataSource.saveImages(data)
                        // convert completable back to Observable in order to use 'materialize'
                        .toObservable<List<ImageData>>()
                        // in order to trigger 'map'
                        .materialize()
                        // in order to return 'element' to main steam, not 'completed'
                        .map { data }
                } else {
                    Observable.empty()
                }
            }
    }

}
