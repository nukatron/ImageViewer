package com.nutron.imageviewer.presentation.imagelist

import com.jakewharton.rxrelay2.PublishRelay
import com.nutron.imageviewer.domain.GettingImageUseCase
import com.nutron.imageviewer.module.ext.elements
import com.nutron.imageviewer.module.ext.error
import com.nutron.imageviewer.presentation.entity.ImageUiData
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers


interface ImageListViewModel {

    val input: Input
    val output: Output

    interface Input {
        val active: Consumer<Unit>
        val refresh: Consumer<Unit>
    }

    interface Output {
        val observePhotos: Observable<List<ImageUiData>>
        val observeShowProgress: Observable<Boolean>
        val error: Observable<Throwable>
    }

}

class ImageListViewModelImpl(
    val gettingImageUseCase: GettingImageUseCase
): ImageListViewModel, ImageListViewModel.Input, ImageListViewModel.Output {

    override val input: ImageListViewModel.Input = this
    override val output: ImageListViewModel.Output = this

    // input
    override val active: PublishRelay<Unit> = PublishRelay.create()
    override val refresh: PublishRelay<Unit> = PublishRelay.create()

    // output
    override val observePhotos: Observable<List<ImageUiData>>
    override val observeShowProgress: Observable<Boolean>
    override val error: Observable<Throwable>

    init {
        // prepare input trigger
        val initTrigger = active.share()
        val refreshTrigger = refresh.share()

        // prepare data source
        val initShareObs = initTrigger.observeOn(Schedulers.io())
            .flatMap { gettingImageUseCase.getImages().materialize() }
            .share()

        val refreshShareObs = refreshTrigger.observeOn(Schedulers.io())
            .flatMap { gettingImageUseCase.fetchImages().materialize() }
            .share()

        // prepare progress dialog trigger
        val startActiveProgress = initTrigger.map { true }
        val stopActiveProgress = initShareObs.map { false }
        observeShowProgress = Observable.merge(startActiveProgress, stopActiveProgress)
            .distinctUntilChanged()

        // prepare image output
        observePhotos = Observable.merge(initShareObs.elements(), refreshShareObs.elements())
        // prepare error output
        error = Observable.merge(initShareObs.error(), refreshShareObs.error())
    }

}