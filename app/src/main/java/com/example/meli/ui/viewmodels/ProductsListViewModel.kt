package com.example.meli.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meli.data.apiservice.ProductsApi
import com.example.meli.data.models.ProductModel
import kotlinx.coroutines.launch

/**
 * Este ViewModel mantiene el estado de la pantalla que muestra el resultado de una búsqueda
 * y de consultar los datos de la búsqueda a la clase ProductApiService.kt
 *
 * This viewmodel maintains the status of the screen that shows the result of a search and
 * consulting the search data to the ProductApiService.kt class
 */
class ProductsListViewModel(private val api: ProductsApi) : ViewModel() {
    val status = MutableLiveData<ProductsListScreenStatus>()
    val products = MutableLiveData<List<ProductModel>>()

    fun getProductFiltered(siteId: String, ItemToSearch: String) = viewModelScope.launch {
        status.value = ProductsListScreenStatus.LOADING
        try {
            products.value =
                api.getProductsFiltered(
                    siteId,
                    ItemToSearch
                ).results
            status.value = ProductsListScreenStatus.DONE
        } catch (e: Exception) {
            status.value = ProductsListScreenStatus.ERROR
            products.value = listOf()
        }
    }
}

enum class ProductsListScreenStatus { LOADING, ERROR, DONE }