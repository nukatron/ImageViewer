package com.nutron.imageviewer.presentation.imagelist

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.nutron.imageviewer.R
import com.nutron.imageviewer.data.datasource.ImageRepository
import com.nutron.imageviewer.ext.appComponent
import com.nutron.imageviewer.presentation.imagelist.di.DaggerImageListComponent
import com.nutron.imageviewer.presentation.imagelist.di.ImageListComponentParent
import com.nutron.imageviewer.presentation.imagelist.di.ImageListModule
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ImageListActivity : AppCompatActivity() {

    @Inject lateinit var repository: ImageRepository

    private val disposeBag = CompositeDisposable()

    internal val imageListComponent by lazy {
        DaggerImageListComponent.builder()
            .parentComponent(appComponent as ImageListComponentParent)
            .ImageListModule(ImageListModule())
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageListComponent.inject(this)

        repository.getImage()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
            onNext = {list ->
                Log.d("ImageListActivity", "list: ${list.joinToString { it.toString() }}")
            },
            onError = { error ->
                Log.e("ImageListActivity", "error: ${error.message}")
            }
        ).addTo(disposeBag)

    }


}
