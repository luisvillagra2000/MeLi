package com.example.meli.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meli.data.models.SiteModel
import com.example.meli.data.repositorys.ProductApi
import kotlinx.coroutines.launch

class ProductSearchViewModel : ViewModel() {
    val status = MutableLiveData<ProductSearchScreenStatus>()
    val sites = MutableLiveData<List<SiteModel>>()
    var indexSiteSelected = 0

    fun getSites() {
        viewModelScope.launch {
            status.value = ProductSearchScreenStatus.LOADING
            try {
                sites.value = ProductApi.retrofitService.getSites().sortedBy { it.name }
                indexSiteSelected = 0
                status.value = ProductSearchScreenStatus.DONE
            } catch (e: Exception) {
                status.value = ProductSearchScreenStatus.ERROR
                sites.value = listOf()
            }
        }
    }
}

enum class ProductSearchScreenStatus { LOADING, ERROR, DONE }