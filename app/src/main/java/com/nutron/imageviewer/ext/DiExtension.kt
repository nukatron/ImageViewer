package com.nutron.imageviewer.ext

import androidx.appcompat.app.AppCompatActivity
import com.nutron.imageviewer.MainApplication
import com.nutron.imageviewer.di.AppComponent


val AppCompatActivity.appComponent: AppComponent
    get() = (application as MainApplication).appComponent