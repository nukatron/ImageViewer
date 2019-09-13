package com.nutron.imageviewer.presentation.entity

import android.os.Parcel
import android.os.Parcelable
import java.util.Date


data class ImageUiData(
    val id: String,
    val createDate: Date?,
    val description: String?,
    val width: Int = 0,
    val height: Int = 0,
    val likes: Int = 0,
    val thumbnail: String,
    val imageUrl: String,
    val username: String,
    val userSocialAccount: String?,
    val userProfileThumbnail: String?,
    val userProfile: String?

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readLong().takeIf { it > 0L }?.let { Date(it) },
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeLong(createDate?.time ?: -1L)
        parcel.writeString(description)
        parcel.writeInt(width)
        parcel.writeInt(height)
        parcel.writeInt(likes)
        parcel.writeString(thumbnail)
        parcel.writeString(imageUrl)
        parcel.writeString(username)
        parcel.writeString(userSocialAccount)
        parcel.writeString(userProfileThumbnail)
        parcel.writeString(userProfile)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageUiData> {
        override fun createFromParcel(parcel: Parcel): ImageUiData {
            return ImageUiData(parcel)
        }

        override fun newArray(size: Int): Array<ImageUiData?> {
            return arrayOfNulls(size)
        }
    }
}