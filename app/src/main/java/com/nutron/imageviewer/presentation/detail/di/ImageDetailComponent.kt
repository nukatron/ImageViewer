package com.nutron.imageviewer.presentation.detail.di

import com.nutron.imageviewer.di.ImageDetailScope
import com.nutron.imageviewer.presentation.detail.ImageDetailActivity
import dagger.Component
import dagger.Module


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

}