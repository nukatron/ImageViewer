package com.nutron.imageviewer.mock

import com.nutron.imageviewer.data.entity.ImageData
import com.nutron.imageviewer.data.entity.Urls
import com.nutron.imageviewer.data.entity.User
import com.nutron.imageviewer.data.entity.UserProfile
import com.nutron.imageviewer.presentation.entity.ImageUiData
import java.util.*


class MockDataProvider {

    fun provideImageDataList(): List<ImageData> {
        val img1 = provideImageData()
            .copy(
                id = "image-id-1",
                user = provideUser().copy(id = "user-id-1"),
                urls = provideUrlData().copy(regular = "https://image.com/regular/1")
            )

        val img2 = provideImageData()
            .copy(
                id = "image-id-2",
                user = provideUser().copy(id = "user-id-2"),
                urls = provideUrlData().copy(regular = "https://image.com/regular/2")
            )

        return arrayListOf(img1, img2)
    }

    fun provideImageUiDataList(): List<ImageUiData> {
        val img1 = provideImageUiData()
            .copy(id = "image-ui-id-1")

        val img2 = provideImageUiData()
            .copy(id = "image-ui-id-2")

        return arrayListOf(img1, img2)
    }

    fun provideImageUiData(): ImageUiData {
        return ImageUiData(
            id = "image-ui-id",
            description = "image ui description",
            createDate = Date(),
            width = 100,
            height = 100,
            likes = 1,
            thumbnail = "https://ui-image.com/thumbnail",
            imageUrl = "https://ui-image.com/regular",
            username = "User name",
            userProfile = "https://ui-image.com/userProfile",
            userProfileThumbnail = "https://ui-image.com/userProfileThumbnail",
            userSocialAccount = "@SocialAccount"
        )
    }

    fun provideImageData(): ImageData {
        return ImageData(
            id = "image-id",
            createDate = Date(),
            height = 1920,
            width = 1024,
            description = "description",
            altDescription = "alternative description",
            like = 1,
            urls = provideUrlData(),
            user = provideUser()
        )
    }

    fun provideUser(): User {
        return User(
            id = "user-id",
            name = "User Name",
            instagramUserName = "@Instagram",
            twitterUserName = "@Twitter",
            profile = provideUserProfile()
        )
    }

    fun provideUrlData(): Urls {
        return Urls(
            "https://image.com/regular",
            "https://image.com/thumbnail"
        )
    }

    fun provideUserProfile(): UserProfile {
        return UserProfile(
            "https://user.com/small",
            "https://user.com/medium"
        )
    }

}