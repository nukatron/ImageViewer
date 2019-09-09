package com.nutron.imageviewer.presentation.imagelist.di

import com.nutron.imageviewer.data.datasource.ImageRepository


interface ImageListComponentParent {

    fun provideImageRepository(): ImageRepository
}