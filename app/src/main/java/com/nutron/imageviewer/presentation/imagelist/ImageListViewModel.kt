package com.nutron.imageviewer.presentation.imagelist

import com.nutron.imageviewer.domain.GettingImageUseCase
import com.nutron.imageviewer.presentation.entity.ImageUiData
import io.reactivex.Observable


interface ImageListViewModel {
    fun observeImages(): Observable<List<ImageUiData>>
}

class ImageListViewModelImpl(
    val gettingImageUseCase: GettingImageUseCase
): ImageListViewModel {

    override fun observeImages(): Observable<List<ImageUiData>> {
        return gettingImageUseCase.getImages()
    }

}