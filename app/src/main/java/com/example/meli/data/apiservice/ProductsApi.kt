package com.example.meli.data.apiservice

import com.example.meli.data.models.ProductsByIdResponse
import com.example.meli.data.models.ProductsFilterResponse
import com.example.meli.data.models.SiteModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * ProductsApi es una interface que sirve para especificar los endpoints y
 * los modelos que retorna estos endpoints
 *
 * ProductsApi is an interface that serves to specify the endpoints
 * and models that these endpoint returns
 */
interface ProductsApi {
    @GET("/sites/{site_id}/search")
    suspend fun getProductsFiltered(
        @Path(value = "site_id", encoded = true) siteId: String,
        @Query("q") ItemToSearch: String
    ): ProductsFilterResponse

    @GET("/items")
    suspend fun getProduct(@Query("ids") id: String): List<ProductsByIdResponse>

    @GET("/sites/")
    suspend fun getSites(): List<SiteModel>
}
