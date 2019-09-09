package com.nutron.imageviewer.domain

import com.nutron.imageviewer.data.datasource.ImageRepository
import com.nutron.imageviewer.presentation.entity.ImageUiData
import io.reactivex.Observable


interface GettingImageUseCase {
    fun getImages(): Observable<List<ImageUiData>>
}

class GettingImageUseCaseImpl(
    val imageRepository: ImageRepository,
    val imageDataMapper: ImageDataMapper
) : GettingImageUseCase {

    override fun getImages(): Observable<List<ImageUiData>> {
        return imageRepository.getImage().map { list ->
            list.map { imageDataMapper.mapToUiData(it) }
        }
    }
}