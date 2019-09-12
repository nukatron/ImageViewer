package com.nutron.imageviewer.domain

import com.nutron.imageviewer.data.entity.ImageData
import com.nutron.imageviewer.data.entity.User
import com.nutron.imageviewer.presentation.entity.ImageUiData
import java.util.*


interface ImageDataMapper {
    fun mapToUiData(imageData: ImageData): ImageUiData
}

class ImageDataMapperImpl: ImageDataMapper {

    override fun mapToUiData(imageData: ImageData): ImageUiData {
        return ImageUiData(
            id = imageData.id ?: "",
            createDate = imageData.createDate ?: Date(),
            description = imageData.description ?: imageData.altDescription,
            width = imageData.width,
            height = imageData.height,
            likes = imageData.like,
            thumbnail = imageData.urls?.thumbnail ?: "",
            imageUrl = imageData.urls?.regular ?: "",
            username = getUserName(imageData?.user),
            userSocialAccount = getSocialAccount(imageData.user),
            userProfileThumbnail = imageData.user?.profile?.small,
            userProfile = imageData.user?.profile?.medium
        )
    }

    private fun getUserName(user: User?): String {
        return user?.name ?: (getSocialAccount(user) ?: "")
    }

    private fun getSocialAccount(user: User?): String? {
        return user?.twitterUserName ?: user?.instagramUserName
    }

}