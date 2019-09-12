package com.nutron.imageviewer.domain

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.nutron.imageviewer.data.datasource.ImageRepository
import com.nutron.imageviewer.mock.MockDataProvider
import io.reactivex.Observable
import org.junit.Test

class GettingImageUseCaseTest {

    val mockDataProvider = MockDataProvider()
    val imageRepository: ImageRepository = mock()
    val imageDataMapper: ImageDataMapper = mock()

    val useCase: GettingImageUseCase = GettingImageUseCaseImpl(
        imageRepository,
        imageDataMapper
    )

    @Test
    fun `test get image from repository`() {
        val imageDataList = listOf(mockDataProvider.provideImageData())
        val imageUiData = mockDataProvider.provideImageUiData()
        doReturn(Observable.just(imageDataList)).whenever(imageRepository).getImage()
        doReturn(imageUiData).whenever(imageDataMapper).mapToUiData(any())
        useCase.getImages()
            .test()
            .assertValue(listOf(imageUiData))
    }

    @Test
    fun `test fetch image from remote by repository`() {
        val imageDataList = listOf(mockDataProvider.provideImageData())
        val imageUiData = mockDataProvider.provideImageUiData()
        doReturn(Observable.just(imageDataList)).whenever(imageRepository).fetchFromRemote()
        doReturn(imageUiData).whenever(imageDataMapper).mapToUiData(any())
        useCase.fetchImages()
            .test()
            .assertValue(listOf(imageUiData))
    }
}