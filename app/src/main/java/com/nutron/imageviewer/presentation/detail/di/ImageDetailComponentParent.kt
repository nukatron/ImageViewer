package com.nutron.imageviewer.presentation.detail.di

import com.nutron.imageviewer.extdi.ImageLoader


interface ImageDetailComponentParent {

    fun provideImageLoader(): ImageLoader
}