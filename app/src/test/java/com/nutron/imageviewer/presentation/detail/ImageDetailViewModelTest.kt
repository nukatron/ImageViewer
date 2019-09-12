package com.nutron.imageviewer.presentation.detail

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.nutron.imageviewer.R
import com.nutron.imageviewer.mock.MockDataProvider
import com.nutron.imageviewer.module.extdi.ResourceProvider
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test

class ImageDetailViewModelTest {

    val mockNoData = "-"
    val resourceProvider: ResourceProvider = mock()
    val mockDataProvider = MockDataProvider()
    private lateinit var viewModel : ImageDetailViewModel

    @Before
    fun setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler{ Schedulers.trampoline() }
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }

        doReturn(mockNoData).whenever(resourceProvider).getString(R.string.detail_no_data)

        viewModel = ImageDetailViewModelImpl(resourceProvider)
    }

    @After
    fun tearDown() {
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
    }

    @Test
    fun `test observe photo url`() {

        val imageUiData = mockDataProvider.provideImageUiData()
        val progressObserver = viewModel.output.observePhoto.test()
        viewModel.input.initData.accept(imageUiData)
        progressObserver.assertValue(imageUiData.imageUrl)
    }

    @Test
    fun `test observe user photo url`() {

        val imageUiData = mockDataProvider.provideImageUiData()
        val progressObserver = viewModel.output.observeUserPhoto.test()
        viewModel.input.initData.accept(imageUiData)
        progressObserver.assertValue(imageUiData.userProfile)
    }

    @Test
    fun `test observe user photo url withh null value should return empty string`() {

        val imageUiData = mockDataProvider.provideImageUiData().copy(userProfile = null)
        val progressObserver = viewModel.output.observeUserPhoto.test()
        viewModel.input.initData.accept(imageUiData)
        progressObserver.assertValue("")
    }

    @Test
    fun `test observe user name`() {
        val imageUiData = mockDataProvider.provideImageUiData()
        val progressObserver = viewModel.output.observeUserName.test()
        viewModel.input.initData.accept(imageUiData)
        progressObserver.assertValue(imageUiData.username)
    }

    @Test
    fun `test observe social account`() {
        val imageUiData = mockDataProvider.provideImageUiData()
        val progressObserver = viewModel.output.observeSocialAccount.test()
        viewModel.input.initData.accept(imageUiData)
        progressObserver.assertValue(imageUiData.userSocialAccount)
    }

    @Test
    fun `test observe social account with no data should return -`() {
        val imageUiData = mockDataProvider.provideImageUiData().copy(userSocialAccount = null)
        val progressObserver = viewModel.output.observeSocialAccount.test()
        viewModel.input.initData.accept(imageUiData)
        progressObserver.assertValue(mockNoData)
    }

    @Test
    fun `test observe photo detail`() {
        val imageIdTxt = "Image id:"
        val descriptionTxt = "Description"
        val sizeTxt = "Size:"
        val dateFormat = "2019-09-11 09:11:22PM"
        val createAtTxt = "CreateAt:"
        doReturn(imageIdTxt).whenever(resourceProvider).getString(R.string.detail_image_id)
        doReturn(descriptionTxt).whenever(resourceProvider).getString(R.string.detail_image_description)
        doReturn(sizeTxt).whenever(resourceProvider).getString(R.string.detail_image_size)
        doReturn(createAtTxt).whenever(resourceProvider).getString(R.string.detail_image_create_at)
        doReturn(dateFormat).whenever(resourceProvider).parseDate(any(), any())

        val imageUiData = mockDataProvider.provideImageUiData()
        val progressObserver = viewModel.output.observePhotoDetails.test()
        viewModel.input.initData.accept(imageUiData)
        progressObserver.assertValues(
            Pair(imageIdTxt, imageUiData.id),
            Pair(sizeTxt, "${imageUiData.width}x${imageUiData.height}"),
            Pair(descriptionTxt, imageUiData.description ?: mockNoData),
            Pair(createAtTxt, dateFormat)
        )
    }


}