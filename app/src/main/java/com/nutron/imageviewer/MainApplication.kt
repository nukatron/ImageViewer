package com.nutron.imageviewer

import android.app.Application
import com.nutron.imageviewer.di.AppComponent
import com.nutron.imageviewer.di.AppModule
import com.nutron.imageviewer.di.DaggerAppComponent
import com.nutron.imageviewer.di.DataSourceModule
import com.nutron.imageviewer.di.NetworkModule


class MainApplication: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = initAppComponent()
    }

    private fun initAppComponent(): AppComponent {
        return DaggerAppComponent.builder()
            .bindApplication(this)
            .appModule(AppModule())
            .networkModule(NetworkModule())
            .dataSourceModule(DataSourceModule())
            .build()
    }

}