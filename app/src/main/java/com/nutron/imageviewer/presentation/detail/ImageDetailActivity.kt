package com.nutron.imageviewer.presentation.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.nutron.imageviewer.R
import com.nutron.imageviewer.module.ext.appComponent
import com.nutron.imageviewer.module.extdi.ImageLoader
import com.nutron.imageviewer.presentation.detail.di.DaggerImageDetailComponent
import com.nutron.imageviewer.presentation.detail.di.ImageDetailComponentParent
import com.nutron.imageviewer.presentation.detail.di.ImageDetailModule
import com.nutron.imageviewer.presentation.entity.ImageUiData
import com.nutron.imageviewer.presentation.widget.TitleDetailCustomViewGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

private const val EXTRA_IMAGE_DATA = "extra_image_data"

class ImageDetailActivity : AppCompatActivity() {

    @Inject lateinit var imageLoader: ImageLoader
    @Inject lateinit var viewModel: ImageDetailViewModel

    private val detailFullImg by lazy<AppCompatImageView>{ findViewById(R.id.detail_image_img) }
    private val userProfileImg by lazy<AppCompatImageView> { findViewById(R.id.detail_user_profile_img) }
    private val userName by lazy<AppCompatTextView> { findViewById(R.id.detail_user_name_txt) }
    private val userSocialAccount by lazy<AppCompatTextView> { findViewById(R.id.detail_user_social_account_txt) }
    private val detailContainer by lazy<LinearLayout> { findViewById(R.id.detail_photo_detail_container) }

    private val disposeBag = CompositeDisposable()
    private var imageData: ImageUiData? = null

    companion object {
        fun createIntent(context: Context, data: ImageUiData): Intent {
            return Intent(context, ImageDetailActivity::class.java).apply {
                putExtra(EXTRA_IMAGE_DATA, data)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)

        initDependencies()
        initOutput()
        initInput()
    }

    private fun initDependencies() {
        DaggerImageDetailComponent.builder()
            .parentComponent(appComponent as ImageDetailComponentParent)
            .imageDetailModule(ImageDetailModule())
            .build()
            .inject(this)
    }

    private fun initInput() {
        if(intent.hasExtra(EXTRA_IMAGE_DATA)) {
            imageData = intent.getParcelableExtra(EXTRA_IMAGE_DATA)
        }
        imageData?.let { viewModel.input.initData.accept(it) }
    }

    private fun initOutput() {
        viewModel.output.observePhoto
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                imageLoader.load(it)
                    .placeholder(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .noFade()
                    .into(detailFullImg)
            }.addTo(disposeBag)

        viewModel.output.observeUserPhoto
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                imageLoader.load(it)
                    .placeholder(R.drawable.ic_avatar)
                    .circleCrop()
                    .into(userProfileImg)
            }.addTo(disposeBag)

        viewModel.output.observeUserName
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                userName.text = it
            }.addTo(disposeBag)

        viewModel.output.observeSocialAccount
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                userSocialAccount.text = it
            }.addTo(disposeBag)

        viewModel.output.observePhotoDetails
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {(title, detail) ->
                detailContainer.addView(createTitleDetailCustomView(title, detail))
            }.addTo(disposeBag)

    }

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.clear()
    }

    private fun createTitleDetailCustomView(
        title: String,
        detail: String
    ): TitleDetailCustomViewGroup {
        return TitleDetailCustomViewGroup(this).apply {
            setTitle(title)
            setDetail(detail)
        }
    }
}
