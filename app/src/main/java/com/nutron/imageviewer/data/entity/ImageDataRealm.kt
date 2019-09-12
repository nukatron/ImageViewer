package com.nutron.imageviewer.data.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

interface RealmDataConverter<D, R> {

    fun dataToRealm(data: D) : R
    fun realmToData(): D
}

open class ImageDataRealm: RealmObject(), RealmDataConverter<ImageData, ImageDataRealm> {

    @PrimaryKey open var id: String? = null
    open var createDate: Date? = null
    open var description: String? = null
    open var altDescription: String? = null
    open var width: Int = 0
    open var height: Int = 0
    open var like: Int = 0
    open var urls: UrlsRealm? = null
    open var user: UserRealm? = null

    override fun dataToRealm(data: ImageData): ImageDataRealm {
        this.id = data.id
        this.createDate = data.createDate
        this.description = data.description
        this.altDescription = data.altDescription
        this.width = data.width
        this.height = data.height
        this.like = data.like
        this.urls = data.urls?.let { UrlsRealm().dataToRealm(it) }
        this.user = data.user?.let { UserRealm().dataToRealm(it) }
        return this
    }

    override fun realmToData(): ImageData {
        return ImageData(
            id,
            createDate,
            description,
            altDescription,
            width,
            height,
            like,
            urls?.realmToData(),
            user?.realmToData()
        )
    }

}

open class UrlsRealm: RealmObject(), RealmDataConverter<Urls?, UrlsRealm> {

    open var regular: String? = null
    open var thumbnail: String? = null

    override fun dataToRealm(data: Urls?): UrlsRealm {
        regular = data?.regular
        thumbnail = data?.thumbnail
        return this
    }

    override fun realmToData(): Urls {
        return Urls(regular, thumbnail)
    }
}

open class UserRealm : RealmObject(), RealmDataConverter<User?, UserRealm> {

    open var id: String? = null
    open var name: String? = null
    open var profile: UserProfileRealm? = null
    open var twitterUserName: String? = null
    open var instagramUserName: String? = null

    override fun dataToRealm(data: User?): UserRealm {
        this.id = data?.id
        this.name = data?.name
        this.profile = data?.profile?.let { UserProfileRealm().dataToRealm(it) }
        this.twitterUserName = data?.twitterUserName
        this.instagramUserName = data?.instagramUserName
        return this
    }

    override fun realmToData(): User {
        return User(id, name, profile?.realmToData(), twitterUserName, instagramUserName)
    }
}

open class UserProfileRealm : RealmObject(), RealmDataConverter<UserProfile?, UserProfileRealm> {

    open var small: String? = null
    open var medium: String? = null

    override fun dataToRealm(data: UserProfile?): UserProfileRealm {
        this.small = data?.small
        this.medium = data?.medium
        return this
    }

    override fun realmToData(): UserProfile {
        return UserProfile(small, medium)
    }
}