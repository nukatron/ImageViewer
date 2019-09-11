package com.nutron.imageviewer.presentation.imagelist

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.nutron.imageviewer.R
import com.nutron.imageviewer.ext.appComponent
import com.nutron.imageviewer.extdi.ImageLoader
import com.nutron.imageviewer.presentation.detail.ImageDetailActivity
import com.nutron.imageviewer.presentation.entity.ImageUiData
import com.nutron.imageviewer.presentation.imagelist.di.DaggerImageListComponent
import com.nutron.imageviewer.presentation.imagelist.di.ImageListComponentParent
import com.nutron.imageviewer.presentation.imagelist.di.ImageListModule
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ImageListActivity : AppCompatActivity(), OnImageListItemClickListener {

    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.image_list_rcv) }

    private val TAG = "ImageListActivity"

    @Inject lateinit var imageLoader: ImageLoader
    @Inject lateinit var viewModel: ImageListViewModel
    private lateinit var viewAdapter: ImageListAdapter
    private lateinit var viewManager: StaggeredGridLayoutManager

    private val disposeBag = CompositeDisposable()

    internal val imageListComponent by lazy {
        DaggerImageListComponent.builder()
            .parentComponent(appComponent as ImageListComponentParent)
            .imageListModule(ImageListModule())
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_list)
        initDependencies()
        initView()
        initData()
    }

    private fun initDependencies() {
        imageListComponent.inject(this)
    }

    private fun initView() {
        viewManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        viewAdapter = ImageListAdapter(imageLoader, this)
        recyclerView.adapter = viewAdapter
        recyclerView.layoutManager = viewManager
        recyclerView.addItemDecoration(SpacesItemDecoration(resources.getDimension(R.dimen.small_margin).toInt()))
    }

    private fun initData() {
        viewModel.observeImages()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {list ->
                    viewAdapter.updateData(list)
                },
                onError = {
                        err -> Log.e(TAG, "error when get images: ${err.message}")
                }
            ).addTo(disposeBag)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.clear()
    }

    override fun onImageItemClickListener(position: Int, data: ImageUiData) {
        val intent = ImageDetailActivity.createIntent(this, data)
        startActivity(intent)
    }


}
