package com.example.meli.di

import com.example.meli.data.apiservice.ProductsApi
import com.example.meli.ui.viewmodels.ProductDetailsViewModel
import com.example.meli.ui.viewmodels.ProductSearchViewModel
import com.example.meli.ui.viewmodels.ProductsListViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {
    single<ProductsApi> {
        Retrofit.Builder()
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .add(KotlinJsonAdapterFactory())
                        .build()
                )
            )
            .baseUrl("https://api.mercadolibre.com/")
            .build().create(ProductsApi::class.java)
    }

    viewModel { ProductDetailsViewModel(get()) }

    viewModel { ProductSearchViewModel(get()) }

    viewModel { ProductsListViewModel(get()) }
}
