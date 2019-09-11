package com.nutron.imageviewer.di

import android.content.Context
import com.nutron.imageviewer.presentation.detail.di.ImageDetailComponentParent
import com.nutron.imageviewer.presentation.imagelist.di.ImageListComponentParent
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(modules = [AppModule::class, NetworkModule::class, DataSourceModule::class])
interface AppComponent: ImageListComponentParent, ImageDetailComponentParent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun bindApplication(context: Context): Builder

        fun appModule(module: AppModule): Builder
        fun networkModule(module: NetworkModule): Builder
        fun dataSourceModule(module: DataSourceModule): Builder
        fun build(): AppComponent
    }
}