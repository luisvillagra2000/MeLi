package com.example.meli.data.repositorys;

import com.example.meli.data.models.ProductModel
import com.example.meli.data.models.SiteModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://api.mercadolibre.com/"
private const val SITE_ID = "MLA"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ProductsApiService {
    @GET("/sites/{site_id}/search")
    suspend fun getProductsFiltered(@Path(value = "site_id", encoded = true) siteId : String, @Query("q") ItemToSearch: String): ProductsFilterResponse

    @GET("/items")
    suspend fun getProduct(@Query("ids") id: String): List<ProductsByIdResponse>

    @GET("/sites/")
    suspend fun getSites(): List<SiteModel>
}

data class ProductsFilterResponse(val results: List<ProductModel>)

data class ProductsByIdResponse(val body: ProductModel)
object ProductApi {
    val retrofitService: ProductsApiService by lazy { retrofit.create(ProductsApiService::class.java) }
}
