package com.nutron.imageviewer.data.datasource.local

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.nutron.imageviewer.data.entity.ImageData
import com.nutron.imageviewer.mock.MockDataProvider
import io.realm.Realm
import org.junit.Test

class ImageLocalDataSourceTest {

    val realmHelper: RealmHelper = mock()
    val localDataSource = ImageLocalDataSource(realmHelper)
    val mockDataProvider = MockDataProvider()

    @Test
    fun `test get image from persistent storage when it has data`() {
        val images = mockDataProvider.provideImageDataList()
        doReturn(images).whenever(realmHelper).query(any<(r: Realm) -> List<ImageData>>())
        val testObserver = localDataSource.getImages().test()
        testObserver.assertValue(images)
    }

    @Test
    fun `test get image from persistent storage when it has no data`() {
        val images = arrayListOf<ImageData>()
        doReturn(images).whenever(realmHelper).query(any<(r: Realm) -> List<ImageData>>())
        val testObserver = localDataSource.getImages().test()
        testObserver.assertNoValues()
    }

    @Test
    fun `test save images`() {
        val images = arrayListOf<ImageData>()
        val testObserver = localDataSource.saveImages(images).test()
        verify(realmHelper).save(any())
        testObserver.assertComplete()
    }
}