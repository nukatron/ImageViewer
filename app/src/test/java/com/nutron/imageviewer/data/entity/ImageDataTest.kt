package com.nutron.imageviewer.data.entity

import com.nutron.imageviewer.mock.MockDataProvider
import org.junit.Assert
import org.junit.Test

class ImageDataTest {

    val mockProvider = MockDataProvider()

    @Test
    fun `test convert data object to realm object`() {
        val imageData = mockProvider.provideImageData()
        val realmObj = ImageDataRealm().dataToRealm(imageData)
        Assert.assertEquals(imageData.id, realmObj.id)
        Assert.assertEquals(imageData.createDate, realmObj.createDate)
        Assert.assertEquals(imageData.description, realmObj.description)
        Assert.assertEquals(imageData.altDescription, realmObj.altDescription)
        Assert.assertEquals(imageData.height, realmObj.height)
        Assert.assertEquals(imageData.width, realmObj.width)
        Assert.assertEquals(imageData.like, realmObj.like)
        Assert.assertEquals(imageData.urls?.thumbnail, realmObj.urls?.thumbnail)
        Assert.assertEquals(imageData.urls?.thumbnail, realmObj.urls?.thumbnail)
        Assert.assertEquals(imageData.user?.id, realmObj.user?.id)
        Assert.assertEquals(imageData.user?.twitterUserName, realmObj.user?.twitterUserName)
        Assert.assertEquals(imageData.user?.instagramUserName, realmObj.user?.instagramUserName)
        Assert.assertEquals(imageData.user?.name, realmObj.user?.name)
        Assert.assertEquals(imageData.user?.profile?.medium, realmObj.user?.profile?.medium)
        Assert.assertEquals(imageData.user?.profile?.small, realmObj.user?.profile?.small)


    }

    @Test
    fun `test convert realm object to data object`() {
        val imageData = mockProvider.provideImageData()
        val realmObj = ImageDataRealm().dataToRealm(imageData)
        val result = realmObj.realmToData()
        Assert.assertEquals(result, imageData)
    }
}