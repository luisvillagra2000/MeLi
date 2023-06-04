package com.example.meli.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meli.data.models.ProductModel
import com.example.meli.data.repositorys.ProductApi
import kotlinx.coroutines.launch

class ProductsListViewModel : ViewModel() {
    val status = MutableLiveData<ProductsListScreenStatus>()
    val products = MutableLiveData<List<ProductModel>>()

    fun getProductFiltered(siteId: String, ItemToSearch: String) {
        viewModelScope.launch {
            status.value = ProductsListScreenStatus.LOADING
            try {
                products.value =
                    ProductApi.retrofitService.getProductsFiltered(
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
}

enum class ProductsListScreenStatus { LOADING, ERROR, DONE }