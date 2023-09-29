package com.example.meli.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.meli.data.apiservice.ProductsApi
import com.example.meli.data.models.SiteModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductSearchViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val api = mockk<ProductsApi>()
    private val testDispatcher = UnconfinedTestDispatcher()
    private val viewModel = ProductSearchViewModel(api)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getSites success`() = runTest(testDispatcher) {
        coEvery { api.getSites() }.returns(siteList)

        viewModel.getSites()

        assert(viewModel.status.value == ProductSearchScreenStatus.DONE)
        assert(viewModel.sites.value == siteList)

    }

    @Test
    fun `getSites error`() = runTest(testDispatcher) {
        coEvery { api.getSites() }.throws(Exception())

        viewModel.getSites()

        assert(viewModel.status.value == ProductSearchScreenStatus.ERROR)
        assert(viewModel.sites.value?.isEmpty() == true)
    }


    //mock data
    private val siteList = listOf(
        SiteModel(id = "1", name = "Name 1"),
        SiteModel(id = "2", name = "Name 2"),
        SiteModel(id = "3", name = "Name 3")
    )
}

