package com.nutron.imageviewer.data.datasource.local

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.nutron.imageviewer.data.entity.ImageData
import com.nutron.imageviewer.data.entity.ImageDataRealm
import com.nutron.imageviewer.mock.MockDataProvider
import org.junit.Test

class ImageLocalDataSourceTest {

    val realmHelper: RealmHelper = mock()
    val localDataSource = ImageLocalDataSource(realmHelper)
    val mockDataProvider = MockDataProvider()

    @Test
    fun `test get image from persistent storage when it has data`() {
        val images = mockDataProvider.provideImageDataList()
        doReturn(images).whenever(realmHelper).query<ImageDataRealm, ImageData>(any())
        val testObserver = localDataSource.getImages().test()
        testObserver.assertValue(images)
        verify(realmHelper).query<ImageDataRealm, ImageData>(any())
        verify(realmHelper).close()
    }

    @Test
    fun `test get image from persistent storage when it has no data`() {
        val images = arrayListOf<ImageData>()
        doReturn(images).whenever(realmHelper).query<ImageDataRealm, ImageData>(any())
        val testObserver = localDataSource.getImages().test()
        testObserver.assertNoValues()
        verify(realmHelper).query<ImageDataRealm, ImageData>(any())
        verify(realmHelper).close()
    }

    @Test
    fun `test save images`() {
        val images = mockDataProvider.provideImageDataList()
        val testObserver = localDataSource.saveImages(images).test()
        testObserver.assertComplete()
        verify(realmHelper).beginTransaction()
        verify(realmHelper).clear(ImageDataRealm::class.java)
        verify(realmHelper, times(2)).upsert<ImageDataRealm>(any())
        verify(realmHelper).commitTransaction()
        verify(realmHelper).close()
    }
}