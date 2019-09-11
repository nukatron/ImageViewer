package com.nutron.imageviewer.ext

import androidx.appcompat.app.AppCompatActivity
import com.nutron.imageviewer.MainApplication
import com.nutron.imageviewer.di.AppComponent
import com.nutron.imageviewer.presentation.detail.ImageDetailActivity


val AppCompatActivity.appComponent: AppComponent
    get() = (application as MainApplication).appComponent

val ImageDetailActivity.appComponent: AppComponent
    get() = (application as MainApplication).appComponent