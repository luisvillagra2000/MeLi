package com.example.meli.data.apiservice;

import com.example.meli.data.models.ProductModel
import com.example.meli.data.models.ProductsByIdResponse
import com.example.meli.data.models.ProductsFilterResponse
import com.example.meli.data.models.SiteModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Esta clase se encarga de obtener los datos y parsearlos a un modelo conocido
 *
 * This class is responsible for obtaining the data and parsing them to a known model
 */
private const val BASE_URL = "https://api.mercadolibre.com/"

/**
 * Moshi se encarga de parsear JSONs a modelos
 *
 * Moshi is responsible for parseing JSONs to models
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Retrofit se encarga de obtener el JSON de una API y combinado con una Moshi
 * obtiene los datos y lo convierte en un modelo
 *
 * Retropit is responsible for obtaining the JSON from an API
 * and combined with a Moshi obtains the data and becomes a model
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

/**
 * ProductsApiService es una interface que sirve para especificar los endpoints y
 * los modelos que retorna estos endpoints
 *
 * ProductsApiService is an interface that serves to specify the endpoints
 * and models that these endpoint returns
 */
interface ProductsApiService {
    @GET("/sites/{site_id}/search")
    suspend fun getProductsFiltered(
        @Path(value = "site_id", encoded = true) siteId : String,
        @Query("q") ItemToSearch: String): ProductsFilterResponse

    @GET("/items")
    suspend fun getProduct(@Query("ids") id: String): List<ProductsByIdResponse>

    @GET("/sites/")
    suspend fun getSites(): List<SiteModel>
}

/**
 * ProductApi se encarga de asociar la interface (con los endpoints) a retrofit y
 * dejar disponible una variable donde podes obtener las API responses
 *
 * ProductApi is responsible for associating the interface (with the endpoints) to retrofit
 * and leaving a variable available where you can obtain the API responses
 */
object ProductApi {
    val retrofitService: ProductsApiService by lazy { retrofit.create(ProductsApiService::class.java) }
}
