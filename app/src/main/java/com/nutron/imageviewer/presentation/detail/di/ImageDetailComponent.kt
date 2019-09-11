package com.nutron.imageviewer.presentation.detail.di

import com.nutron.imageviewer.module.di.ImageDetailScope
import com.nutron.imageviewer.module.extdi.ResourceProvider
import com.nutron.imageviewer.presentation.detail.ImageDetailActivity
import com.nutron.imageviewer.presentation.detail.ImageDetailViewModel
import com.nutron.imageviewer.presentation.detail.ImageDetailViewModelImpl
import dagger.Component
import dagger.Module
import dagger.Provides


@ImageDetailScope
@Component(dependencies = [ImageDetailComponentParent::class], modules = [ImageDetailModule::class])
interface ImageDetailComponent {

    fun inject(imageDetailActivity: ImageDetailActivity)

    @Component.Builder
    interface Builder {
        fun parentComponent(parent: ImageDetailComponentParent): Builder
        fun imageDetailModule(module: ImageDetailModule): Builder
        fun build(): ImageDetailComponent
    }
}

@Module
class ImageDetailModule {

    @ImageDetailScope
    @Provides
    fun provideViewModel(
        resourceProvider: ResourceProvider
    ): ImageDetailViewModel = ImageDetailViewModelImpl(resourceProvider)
}