package com.nutron.imageviewer.data.datasource.remote

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.nutron.imageviewer.mock.MockDataProvider
import io.reactivex.Observable
import org.junit.Test

class ImageRemoteDataSourceTest {

    val api = mock<ImageApi>()
    val authConfigProvider = mock<AuthConfigProvider>()

    val mockProvider = MockDataProvider()
    val remoteDataSource = ImageRemoteDataSource(api, authConfigProvider)

    @Test
    fun `test get image from remote`() {
        val response = mockProvider.provideImageDataList()
        doReturn("access-key").whenever(authConfigProvider).getAccessKey()
        doReturn(Observable.just(response)).whenever(api).getImages(any())
        remoteDataSource.getImages()
            .test()
            .assertValue(response)
    }
}