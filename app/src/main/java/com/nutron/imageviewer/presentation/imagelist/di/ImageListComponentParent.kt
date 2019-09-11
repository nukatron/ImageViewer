package com.nutron.imageviewer.presentation.imagelist.di

import com.nutron.imageviewer.data.datasource.ImageRepository
import com.nutron.imageviewer.module.extdi.ImageLoader


interface ImageListComponentParent {

    fun provideImageRepository(): ImageRepository
    fun provideImageLoader(): ImageLoader
}