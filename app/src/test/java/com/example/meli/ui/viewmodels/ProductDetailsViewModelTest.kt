package com.example.meli.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.meli.data.apiservice.ProductsApi
import com.example.meli.data.models.Pictures
import com.example.meli.data.models.ProductModel
import com.example.meli.data.models.ProductsByIdResponse
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
class ProductDetailsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val api = mockk<ProductsApi>()
    private val testDispatcher = UnconfinedTestDispatcher()
    private val viewModel = ProductDetailsViewModel(api)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getProduct success`() = runTest(testDispatcher) {
        coEvery { api.getProduct(productId) }.returns(listOf(ProductsByIdResponse(product)))

        viewModel.getProduct(productId)

        assert(viewModel.status.value == ProductDetailsScreenStatus.DONE)
        assert(viewModel.productSelected.value == product)
    }

    @Test
    fun `getProduct error`() = runTest(testDispatcher) {
        coEvery { api.getProduct(productId) }.throws(Exception())

        viewModel.getProduct(productId)

        assert(viewModel.status.value == ProductDetailsScreenStatus.ERROR)
        assert(viewModel.productSelected.value == null)
    }

    // mock data
    private val productId = "12345"
    private val product = ProductModel(
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
}
