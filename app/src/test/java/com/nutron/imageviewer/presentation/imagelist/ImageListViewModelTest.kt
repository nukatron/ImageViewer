package com.nutron.imageviewer.presentation.imagelist

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.nutron.imageviewer.domain.GettingImageUseCase
import com.nutron.imageviewer.mock.MockDataProvider
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test

class ImageListViewModelTest {

    val usecase: GettingImageUseCase = mock()
    val mockDataProvider = MockDataProvider()
    lateinit var viewModel: ImageListViewModel

    @Before
    fun setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler{ Schedulers.trampoline() }
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        viewModel = ImageListViewModelImpl(usecase)
    }

    @After
    fun tearDown() {
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
    }

    @Test
    fun `test observe photo when init`() {
        val initDataList = mockDataProvider.provideImageUiDataList().map { it.copy(id="init-id") }
        val fetchDataList = mockDataProvider.provideImageUiDataList().map { it.copy(id="fetch-id") }
        doReturn(Observable.just(initDataList)).whenever(usecase).getImages()
        doReturn(Observable.just(fetchDataList)).whenever(usecase).fetchImages()

        val testObserver = viewModel.output.observePhotos.test()
        viewModel.input.active.accept(Unit)
        // photo stream should emit only initDataList
        testObserver.assertValue(initDataList)
    }

    @Test
    fun `test observe photo should get photo list when refresh`() {
        val initDataList = mockDataProvider.provideImageUiDataList().map { it.copy(id="init-id") }
        val fetchDataList = mockDataProvider.provideImageUiDataList().map { it.copy(id="fetch-id") }
        doReturn(Observable.just(initDataList)).whenever(usecase).getImages()
        doReturn(Observable.just(fetchDataList)).whenever(usecase).fetchImages()

        val testObserver = viewModel.output.observePhotos.test()
        viewModel.input.refresh.accept(Unit)
        // photo stream should emit only fetchDataList
        testObserver.assertValue(fetchDataList)
    }

    @Test
    fun `test observe progress bar should show and hide when there are data`() {
        val initDataList = mockDataProvider.provideImageUiDataList().map { it.copy(id="init-id") }
        val fetchDataList = mockDataProvider.provideImageUiDataList().map { it.copy(id="fetch-id") }
        doReturn(Observable.just(initDataList)).whenever(usecase).getImages()
        doReturn(Observable.just(fetchDataList)).whenever(usecase).fetchImages()

        val testObserver = viewModel.output.observeShowProgress.test()
        viewModel.input.active.accept(Unit)
        // progress stream should emit true and then false
        testObserver.assertValues(true, false)
    }

    @Test
    fun `test observe progress bar should NOT show when fetch data`() {
        val initDataList = mockDataProvider.provideImageUiDataList().map { it.copy(id="init-id") }
        val fetchDataList = mockDataProvider.provideImageUiDataList().map { it.copy(id="fetch-id") }
        doReturn(Observable.just(initDataList)).whenever(usecase).getImages()
        doReturn(Observable.just(fetchDataList)).whenever(usecase).fetchImages()

        val testObserver = viewModel.output.observeShowProgress.test()
        viewModel.input.refresh.accept(Unit)
        // progress stream should NOT emit any value
        testObserver.assertNoValues()
    }

    @Test
    fun `test observe error when get image failed`() {
        val err = Exception("error when get data")
        val fetchDataList = mockDataProvider.provideImageUiDataList().map { it.copy(id="fetch-id") }
        doReturn(Observable.error<Exception>(err)).whenever(usecase).getImages()
        doReturn(Observable.just(fetchDataList)).whenever(usecase).fetchImages()

        val testObserver = viewModel.output.error.test()
        viewModel.input.active.accept(Unit)
        // photo stream should emit only initDataList
        testObserver.assertValue(err)
    }

    @Test
    fun `test observe error when fetch image failed`() {
        val err = Exception("error when get data")
        val initDataList = mockDataProvider.provideImageUiDataList().map { it.copy(id="init-id") }
        doReturn(Observable.just(initDataList)).whenever(usecase).getImages()
        doReturn(Observable.error<Exception>(err)).whenever(usecase).fetchImages()

        val testObserver = viewModel.output.error.test()
        viewModel.input.refresh.accept(Unit)
        // photo stream should emit only initDataList
        testObserver.assertValue(err)
    }
}