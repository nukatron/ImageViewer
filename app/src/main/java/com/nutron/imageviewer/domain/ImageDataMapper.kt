package com.nutron.imageviewer.domain

import com.nutron.imageviewer.data.entity.ImageData
import com.nutron.imageviewer.data.entity.User
import com.nutron.imageviewer.presentation.entity.ImageUiData


interface ImageDataMapper {
    fun mapToUiData(imageData: ImageData): ImageUiData
}

class ImageDataMapperImpl: ImageDataMapper {

    override fun mapToUiData(imageData: ImageData): ImageUiData {
        return ImageUiData(
            id = imageData.id ?: "",
            description = imageData.description ?: imageData.altDescription,
            likes = imageData.like,
            thumbnail = imageData.urls.thumbnail ?: "",
            imageUrl = imageData.urls.regular ?: "",
            username = getUserName(imageData.user),
            userProfileThumbnail = imageData.user.profile.small,
            userProfile = imageData.user.profile.medium
        )
    }

    private fun getUserName(user: User): String {
        return user.twitterUserName ?: (user.instagramUserName ?: (user.name ?: ""))
    }

}