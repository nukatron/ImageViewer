package com.nutron.imageviewer.data.entity

import com.nutron.imageviewer.mock.MockDataProvider
import org.junit.Assert
import org.junit.Test

class UrlsRealmTest {

    val mockProvider = MockDataProvider()

    @Test
    fun `test convert data object to realm object`() {
        val urls = mockProvider.provideUrlData()
        val realmObj = UrlsRealm().dataToRealm(urls)
        Assert.assertEquals(urls.thumbnail, realmObj.thumbnail)
        Assert.assertEquals(urls.regular, realmObj.regular)

    }

    @Test
    fun `test convert realm object to data object`() {
        val urls = mockProvider.provideUrlData()
        val realmObj = UrlsRealm().dataToRealm(urls)
        val result = realmObj.realmToData()
        Assert.assertEquals(result, urls)
    }
}