package com.nutron.imageviewer.presentation.detail.di

import com.nutron.imageviewer.module.extdi.ImageLoader
import com.nutron.imageviewer.module.extdi.ResourceProvider


interface ImageDetailComponentParent {

    fun provideImageLoader(): ImageLoader
    fun provideResourceProvider(): ResourceProvider
}