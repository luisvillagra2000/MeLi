package com.example.meli.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meli.data.models.ProductModel
import com.example.meli.data.repositorys.ProductApi
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    val status = MutableLiveData(ApiStatus.LOADING)
    val products = MutableLiveData<List<ProductModel>>()
    val productSelected = MutableLiveData<ProductModel?>()

    fun getProductFiltered(ItemToSearch: String) {
        viewModelScope.launch {
            status.value = ApiStatus.LOADING
            try {
                products.value =
                    ProductApi.retrofitService.getProductsFiltered(ItemToSearch).results
                status.value = ApiStatus.DONE
            } catch (e: Exception) {
                status.value = ApiStatus.ERROR
                products.value = listOf()
            }
        }
    }

    fun getProduct(id: String) {
        viewModelScope.launch {
            status.value = ApiStatus.LOADING
            try {
                productSelected.value = ProductApi.retrofitService.getProduct(id).first().body
                status.value = ApiStatus.DONE
            } catch (e: Exception) {
                status.value = ApiStatus.ERROR
                productSelected.value = null
            }
        }
    }
}

enum class ApiStatus { LOADING, ERROR, DONE }