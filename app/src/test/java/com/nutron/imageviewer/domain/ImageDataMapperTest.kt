package com.nutron.imageviewer.domain

import com.nutron.imageviewer.mock.MockDataProvider
import org.junit.Assert
import org.junit.Test

class ImageDataMapperTest {

    val mockDataProvider = MockDataProvider()
    val mapper: ImageDataMapper = ImageDataMapperImpl()

    @Test
    fun `test convert ImageData to ImageUiData`() {
        val imageData = mockDataProvider.provideImageData()
        val result = mapper.mapToUiData(imageData)
        Assert.assertEquals(imageData.id, result.id)
        Assert.assertEquals(imageData.createDate, result.createDate)
        Assert.assertEquals(imageData.description, result.description)
        Assert.assertEquals(imageData.width, result.width)
        Assert.assertEquals(imageData.height, result.height)
        Assert.assertEquals(imageData.like, result.likes)
        Assert.assertEquals(imageData.urls?.thumbnail, result.thumbnail)
        Assert.assertEquals(imageData.urls?.regular, result.imageUrl)
        Assert.assertEquals(imageData.user?.name, result.username)
        Assert.assertEquals(imageData.user?.twitterUserName, result.userSocialAccount)
        Assert.assertEquals(imageData.user?.profile?.small, result.userProfileThumbnail)
        Assert.assertEquals(imageData.user?.profile?.medium, result.userProfile)
    }

    @Test
    fun `test convert ImageData to ImageUiData with out twitter account`() {
        val imageData = mockDataProvider.provideImageData()
            .copy(user = mockDataProvider.provideUser().copy(twitterUserName = null))
        val result = mapper.mapToUiData(imageData)
        Assert.assertEquals(imageData.user?.instagramUserName, result.userSocialAccount)
    }

    @Test
    fun `test convert ImageData to ImageUiData without social account`() {
        val imageData = mockDataProvider.provideImageData()
            .copy(user = mockDataProvider.provideUser()
                .copy(twitterUserName = null, instagramUserName = null))
        val result = mapper.mapToUiData(imageData)
        Assert.assertNull(result.userSocialAccount)
    }

    @Test
    fun `test convert ImageData to ImageUiData without user name then use twitter account`() {
        val imageData = mockDataProvider.provideImageData()
            .copy(user = mockDataProvider.provideUser().copy(name = null))
        val result = mapper.mapToUiData(imageData)
        Assert.assertEquals(imageData.user?.twitterUserName, result.userSocialAccount)
    }

    @Test
    fun `test convert ImageData to ImageUiData without user name and twitter account then use instagram account`() {
        val imageData = mockDataProvider.provideImageData()
            .copy(user = mockDataProvider.provideUser().copy(name = null, twitterUserName = null))
        val result = mapper.mapToUiData(imageData)
        Assert.assertEquals(imageData.user?.instagramUserName, result.userSocialAccount)
    }
}