package com.example.meli.data.models

data class ProductModel(
    val id: String,
    val title: String,
    val thumbnail: String,
    val permalink: String,
    val currency_id: String,
    val price: Double,
    val pictures: List<Pictures>? = null
)

data class Pictures(val url: String)

data class ProductsFilterResponse(val results: List<ProductModel>)

data class ProductsByIdResponse(val body: ProductModel)