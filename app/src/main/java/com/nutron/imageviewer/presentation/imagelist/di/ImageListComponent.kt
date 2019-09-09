package com.nutron.imageviewer.presentation.imagelist.di

import com.nutron.imageviewer.data.datasource.ImageRepository
import com.nutron.imageviewer.di.ImageListScope
import com.nutron.imageviewer.domain.GettingImageUseCase
import com.nutron.imageviewer.domain.GettingImageUseCaseImpl
import com.nutron.imageviewer.domain.ImageDataMapper
import com.nutron.imageviewer.domain.ImageDataMapperImpl
import com.nutron.imageviewer.presentation.imagelist.ImageListActivity
import dagger.Component
import dagger.Module
import dagger.Provides


@ImageListScope
@Component(dependencies = [ImageListComponentParent::class], modules = [ImageListModule::class])
interface ImageListComponent {

    fun inject(activity: ImageListActivity)

    @Component.Builder
    interface Builder {
        fun parentComponent(parent: ImageListComponentParent): Builder
        fun ImageListModule(module: ImageListModule): Builder
        fun build(): ImageListComponent
    }
}

@Module
class ImageListModule {

    @Provides
    @ImageListScope
    fun provideImageDatamapper(): ImageDataMapper = ImageDataMapperImpl()

    @Provides
    @ImageListScope
    fun provideGettingImageUseCase(
        imageRepository: ImageRepository,
        dataMapper: ImageDataMapper
    ): GettingImageUseCase = GettingImageUseCaseImpl(imageRepository, dataMapper)
}