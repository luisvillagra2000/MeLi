package com.example.meli.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meli.data.models.ProductModel
import com.example.meli.data.repositorys.ProductApi
import kotlinx.coroutines.launch

class ProductDetailsViewModel : ViewModel() {
    val status = MutableLiveData<ProductDetailsScreenStatus>()
    val productSelected = MutableLiveData<ProductModel?>()

    fun getProduct(id: String) {
        viewModelScope.launch {
            status.value = ProductDetailsScreenStatus.LOADING
            try {
                productSelected.value = ProductApi.retrofitService.getProduct(id).first().body
                status.value = ProductDetailsScreenStatus.DONE
            } catch (e: Exception) {
                status.value = ProductDetailsScreenStatus.ERROR
                productSelected.value = null
            }
        }
    }
}

enum class ProductDetailsScreenStatus { LOADING, ERROR, DONE }