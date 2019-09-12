package com.nutron.imageviewer.presentation.detail

import com.jakewharton.rxrelay2.PublishRelay
import com.nutron.imageviewer.R
import com.nutron.imageviewer.module.extdi.ResourceProvider
import com.nutron.imageviewer.presentation.entity.ImageUiData
import io.reactivex.Observable
import io.reactivex.functions.Consumer

private const val SIMPLE_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss a"

interface ImageDetailViewModel {

    val input: Input
    val output: Output

    interface Input {
        val initData: Consumer<ImageUiData>
    }

    interface Output {
        val observePhoto: Observable<String>
        val observeUserPhoto: Observable<String>
        val observeUserName: Observable<String>
        val observeSocialAccount: Observable<String>
        val observePhotoDetails: Observable<Pair<String, String>>
    }
}

class ImageDetailViewModelImpl(
    private val resourceProvider: ResourceProvider
): ImageDetailViewModel, ImageDetailViewModel.Input, ImageDetailViewModel.Output {

    override val input: ImageDetailViewModel.Input = this
    override val output: ImageDetailViewModel.Output = this

    // Input
    override val initData: PublishRelay<ImageUiData> = PublishRelay.create()

    // Output
    override val observePhoto: Observable<String>
    override val observeUserPhoto: Observable<String>
    override val observeUserName: Observable<String>
    override val observeSocialAccount: Observable<String>
    override val observePhotoDetails: Observable<Pair<String, String>>

    init {
        val shareDataObs = initData.share()

        val noData = resourceProvider.getString(R.string.detail_no_data)
        observePhoto = shareDataObs.map { it.imageUrl }
        observeUserPhoto = shareDataObs.map { it.userProfile ?: "" }
        observeUserName = shareDataObs.map { it.username }
        observeSocialAccount = shareDataObs.map { it.userSocialAccount ?: noData }

        observePhotoDetails = Observable.merge(
            shareDataObs.map { Pair(resourceProvider.getString(R.string.detail_image_id), it.id) },
            shareDataObs.map { Pair(resourceProvider.getString(R.string.detail_image_size), "${it.width}x${it.height}") },
            shareDataObs.map { Pair(resourceProvider.getString(R.string.detail_image_description), it.description ?: noData) },
            shareDataObs.map { Pair(
                resourceProvider.getString(R.string.detail_image_create_at),
                it.createDate?.let { date -> resourceProvider.paseDate(date, SIMPLE_DATE_FORMAT) } ?: noData )
            }
        )
    }
}