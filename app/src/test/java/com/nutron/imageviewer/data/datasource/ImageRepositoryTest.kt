package com.nutron.imageviewer.data.datasource

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.nutron.imageviewer.data.entity.ImageData
import com.nutron.imageviewer.mock.MockDataProvider
import io.reactivex.Completable
import io.reactivex.Observable
import org.junit.Test

class ImageRepositoryTest {

    val mockDataProvider = MockDataProvider()
    val localSource: ImageDataSource = mock()
    val remoteSource: ImageDataSource = mock()
    val storeSource: ImageStoreDataSource = mock()
    val repo: ImageRepository = ImageRepositoryImpl(
        localSource,
        remoteSource,
        storeSource
    )

    @Test
    fun `test get image from local and then from remote`() {
        val localData = listOf(
            mockDataProvider.provideImageData().copy(id = "local-1"),
            mockDataProvider.provideImageData().copy(id = "local-2")
        ).sortedBy { it.createDate }
        val remoteData = listOf(
            mockDataProvider.provideImageData().copy(id = "remote-1"),
            mockDataProvider.provideImageData().copy(id = "remote-2")
        ).sortedBy { it.createDate }

        doReturn(Observable.just(localData)).whenever(localSource).getImages()
        doReturn(Observable.just(remoteData)).whenever(remoteSource).getImages()
        doReturn(Completable.complete()).whenever(storeSource).saveImages(any())
        repo.getImage().test()
            .assertValues(localData, remoteData)
        verify(storeSource).saveImages(remoteData)

    }

    @Test
    fun `test get image from local but empty from remote`() {
        val localData = listOf(
            mockDataProvider.provideImageData().copy(id = "local-1"),
            mockDataProvider.provideImageData().copy(id = "local-2")
        ).sortedBy { it.createDate }

        val remoteData = listOf<ImageData>()
        doReturn(Observable.just(localData)).whenever(localSource).getImages()
        doReturn(Observable.just(remoteData)).whenever(remoteSource).getImages()
        doReturn(Completable.complete()).whenever(storeSource).saveImages(any())
        repo.getImage()
            .test()
            .assertValues(localData)
        verify(storeSource, never()).saveImages(remoteData)
    }

    @Test
    fun `test fetch data from disk`() {
        val localData = listOf(mockDataProvider.provideImageData())
        doReturn(Observable.just(localData)).whenever(localSource).getImages()
        repo.fetchFromDisk()
            .test()
            .assertValues(localData)
    }

    @Test
    fun `test fetch from remote and save to local`() {
        val remoteData = mockDataProvider.provideImageDataList()
        doReturn(Observable.just(remoteData)).whenever(remoteSource).getImages()
        doReturn(Completable.complete()).whenever(storeSource).saveImages(any())
        repo.fetchFromRemote()
            .test()
            .assertValues(remoteData)
        verify(storeSource).saveImages(remoteData)
    }

    @Test
    fun `test fetch no image from remote but not save to local`() {
        val remoteData = listOf<ImageData>()
        doReturn(Observable.just(remoteData)).whenever(remoteSource).getImages()
        doReturn(Completable.complete()).whenever(storeSource).saveImages(any())
        repo.fetchFromRemote()
            .test()
            .assertNoValues()
        verify(storeSource, never()).saveImages(remoteData)
    }
}