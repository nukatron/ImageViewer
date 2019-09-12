package com.nutron.imageviewer.presentation.imagelist

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.nutron.imageviewer.R
import com.nutron.imageviewer.module.ext.appComponent
import com.nutron.imageviewer.module.extdi.ImageLoader
import com.nutron.imageviewer.presentation.detail.ImageDetailActivity
import com.nutron.imageviewer.presentation.entity.ImageUiData
import com.nutron.imageviewer.presentation.imagelist.di.DaggerImageListComponent
import com.nutron.imageviewer.presentation.imagelist.di.ImageListComponentParent
import com.nutron.imageviewer.presentation.imagelist.di.ImageListModule
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

private const val TAG = "ImageListActivity"

class ImageListActivity : AppCompatActivity(), OnImageListItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.image_list_rcv) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.image_list_progress) }
    private val rootContainer by lazy { findViewById<ViewGroup>(R.id.image_list_root_container) }
    private val swipeRefreshLayout by lazy { findViewById<SwipeRefreshLayout>(R.id.image_list_swiperefresh) }

    @Inject lateinit var imageLoader: ImageLoader
    @Inject lateinit var viewModel: ImageListViewModel
    private lateinit var viewAdapter: ImageListAdapter
    private lateinit var viewManager: StaggeredGridLayoutManager
    private var snackbar: Snackbar? = null

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
        initOutput()
        initInput()
    }

    private fun initDependencies() {
        imageListComponent.inject(this)
    }

    private fun initView() {
        swipeRefreshLayout.setOnRefreshListener(this)
        viewManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        viewAdapter = ImageListAdapter(imageLoader, this)
        recyclerView.adapter = viewAdapter
        recyclerView.layoutManager = viewManager
        recyclerView.addItemDecoration(SpacesItemDecoration(resources.getDimension(R.dimen.small_margin).toInt()))
    }

    override fun onRefresh() {
        viewModel.input.refresh.accept(Unit)
    }

    fun initOutput() {
        viewModel.output.observePhotos
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                viewAdapter.updateData(it)
                snackbar?.takeIf { snb -> snb.isShown }?.dismiss()
                swipeRefreshLayout.isRefreshing = false
            }
            .addTo(disposeBag)

        viewModel.output.observeShowProgress
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                progressBar.visibility = if(it) View.VISIBLE else View.INVISIBLE
            }.addTo(disposeBag)

        viewModel.output.error
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                swipeRefreshLayout.isRefreshing = false
                snackbar = Snackbar.make(rootContainer, "error: ${it.message}", Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.label_ok) {
                        snackbar?.dismiss()
                    }
                snackbar?.show()
            }.addTo(disposeBag)
    }

    private fun initInput() {
        viewModel.input.active.accept(Unit)
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
