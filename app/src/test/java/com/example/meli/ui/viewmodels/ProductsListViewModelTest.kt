package com.example.meli.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.meli.data.apiservice.ProductsApi
import com.example.meli.data.models.Pictures
import com.example.meli.data.models.ProductModel
import com.example.meli.data.models.ProductsFilterResponse
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
class ProductsListViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val api = mockk<ProductsApi>()
    private val testDispatcher = UnconfinedTestDispatcher()
    private val viewModel = ProductsListViewModel(api)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getProductFiltered success`() = runTest(testDispatcher) {
        coEvery { api.getProductsFiltered(siteId, itemToSearch) }.returns(
            ProductsFilterResponse(
                productList
            )
        )

        viewModel.getProductFiltered(siteId, itemToSearch)

        assert(viewModel.status.value == ProductsListScreenStatus.DONE)
        assert(viewModel.products.value == productList)

    }

    @Test
    fun `getProductFiltered error`() = runTest(testDispatcher) {
        coEvery { api.getProductsFiltered(siteId, itemToSearch) }.throws(Exception())

        viewModel.getProductFiltered(siteId, itemToSearch)

        assert(viewModel.status.value == ProductsListScreenStatus.ERROR)
        assert(viewModel.products.value?.isEmpty() == true)
    }


    //mock data
    private val siteId = "siteId"
    private val itemToSearch = "itemToSearch"
    private val productList = listOf(
        ProductModel(
            id = "12345",
            title = "Ejemplo de Producto",
            thumbnail = "https://ejemplo.com/imagen.jpg",
            permalink = "https://ejemplo.com/producto",
            currency_id = "USD",
            price = 19.99,
            pictures = listOf(
                Pictures("https://ejemplo.com/imagen1.jpg"),
                Pictures("https://ejemplo.com/imagen2.jpg"),
                Pictures("https://ejemplo.com/imagen3.jpg")
            )
        )
    )
}
