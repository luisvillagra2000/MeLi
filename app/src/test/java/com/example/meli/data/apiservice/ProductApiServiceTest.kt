package com.example.meli.data.apiservice

import com.example.meli.data.apiservice.jsons.productJson
import com.example.meli.data.apiservice.jsons.searchJson
import com.example.meli.data.apiservice.jsons.sitesJson
import com.example.meli.data.models.Pictures
import com.example.meli.data.models.ProductModel
import com.example.meli.data.models.ProductsByIdResponse
import com.example.meli.data.models.ProductsFilterResponse
import com.example.meli.data.models.SiteModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


@RunWith(JUnit4::class)
class ProductApiServiceTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var service: ProductsApiService

    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        service = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(mockWebServer.url("/"))
            .build().create(ProductsApiService::class.java)

    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun getProductsFiltered() = runBlocking(Dispatchers.IO) {
        mockWebServer.enqueue(MockResponse().setBody(searchJson))
        val actual = service.getProductsFiltered("", "")
        val expected = ProductsFilterResponse(
            results = listOf(
                ProductModel(
                    id = "MLU617700165",
                    title = "Moto G6 32 Gb Color Negro 3 Gb Ram",
                    thumbnail = "http://http2.mlstatic.com/D_676862-MLU51415800073_092022-O.jpg",
                    permalink = "https://articulo.mercadolibre.com.uy/MLU-617700165-moto-g6-32-gb-color-negro-3-gb-ram-_JM",
                    currency_id = "UYU",
                    price = 3000.0,
                    pictures = null
                ),
                ProductModel(
                    id = "MLU639317198",
                    title = "Moto G6 Play",
                    thumbnail = "http://http2.mlstatic.com/D_674355-MLU69292623442_052023-O.jpg",
                    permalink = "https://articulo.mercadolibre.com.uy/MLU-639317198-moto-g6-play-_JM",
                    currency_id = "UYU",
                    price = 1500.0,
                    pictures = null
                ),
                ProductModel(
                    id = "MLU628835368",
                    title = "Celular Motorola Moto G6, 3gb Ram, 32 Gb Rom, Impecable.",
                    thumbnail = "http://http2.mlstatic.com/D_993548-MLU53944067512_022023-O.jpg",
                    permalink = "https://articulo.mercadolibre.com.uy/MLU-628835368-celular-motorola-moto-g6-3gb-ram-32-gb-rom-impecable-_JM",
                    currency_id = "UYU",
                    price = 4500.0,
                    pictures = null
                )
            )
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun getProduct() = runBlocking(Dispatchers.IO) {
        mockWebServer.enqueue(MockResponse().setBody(productJson))
        val actual = service.getProduct("")
        val expected = listOf(
            ProductsByIdResponse(
                body = ProductModel(
                    id = "MLU639317198",
                    title = "Moto G6 Play",
                    thumbnail = "http://http2.mlstatic.com/D_674355-MLU69292623442_052023-I.jpg",
                    permalink = "https://articulo.mercadolibre.com.uy/MLU-639317198-moto-g6-play-_JM",
                    currency_id = "UYU",
                    price = 1500.0,
                    pictures = listOf(Pictures(url = "http://http2.mlstatic.com/D_674355-MLU69292623442_052023-O.jpg"))
                )
            )
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun getSites() = runBlocking(Dispatchers.IO) {
        mockWebServer.enqueue(MockResponse().setBody(sitesJson))
        val actual = service.getSites()
        val expected = listOf(
            SiteModel(
                id = "MGT",
                name = "Guatemala"
            ),
            SiteModel(
                id = "MNI",
                name = "Nicaragua"
            ),
            SiteModel(
                id = "MRD",
                name = "Dominicana"
            )
        )
        Assert.assertEquals(expected, actual)
    }
}