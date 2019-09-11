package com.nutron.imageviewer.presentation.entity

import android.os.Parcel
import android.os.Parcelable


data class ImageUiData(
    val id: String,
    val description: String?,
    val likes: Int = 0,
    val thumbnail: String,
    val imageUrl: String,
    val username: String,
    val userProfileThumbnail: String?,
    val userProfile: String?

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(description)
        parcel.writeInt(likes)
        parcel.writeString(thumbnail)
        parcel.writeString(imageUrl)
        parcel.writeString(username)
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