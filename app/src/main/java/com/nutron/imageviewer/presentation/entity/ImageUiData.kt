package com.nutron.imageviewer.presentation.entity


data class ImageUiData(
    val id: String,
    val description: String?,
    val likes: Int = 0,
    val thumbnail: String,
    val imageUrl: String,
    val username: String,
    val userProfileThumbnail: String?,
    val userProfile: String?
)