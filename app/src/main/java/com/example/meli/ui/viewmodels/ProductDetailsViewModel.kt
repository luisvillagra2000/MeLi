package com.example.meli.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meli.data.apiservice.ProductsApi
import com.example.meli.data.models.ProductModel
import kotlinx.coroutines.launch

/**
 * Este ViewModel mantiene el estado de la pantalla de búsqueda de un producto
 * y de consultar los datos de un producto a la clase ProductApiService.kt
 *
 * This ViewModel maintains the state of the product detail screen and
 * consulting the data of a product to the ProductApiService.kt class
 */
class ProductDetailsViewModel(private val api: ProductsApi) : ViewModel() {
    val status = MutableLiveData<ProductDetailsScreenStatus>()
    val productSelected = MutableLiveData<ProductModel?>()

    fun getProduct(id: String) = viewModelScope.launch {
        status.value = ProductDetailsScreenStatus.LOADING
        try {
            productSelected.value = api.getProduct(id).first().body
            status.value = ProductDetailsScreenStatus.DONE
        } catch (e: Exception) {
            status.value = ProductDetailsScreenStatus.ERROR
            productSelected.value = null
        }
    }
}

enum class ProductDetailsScreenStatus { LOADING, ERROR, DONE }