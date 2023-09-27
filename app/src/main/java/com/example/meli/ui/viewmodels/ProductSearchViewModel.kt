package com.example.meli.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meli.data.apiservice.ProductsApi
import com.example.meli.data.models.SiteModel
import kotlinx.coroutines.launch

/**
 * Este ViewModel mantiene el estado de la pantalla de búsqueda de un productos y de consultar
 * los sitios en donde MercadoLibre ofrece servicio a la clase ProductApiService.kt
 *
 * This ViewModel maintains the state of the search screen for a product and consulting
 * the sites where Mercadolibre offers service to the ProductApiService.kt class
 */
class ProductSearchViewModel(private val api: ProductsApi) : ViewModel() {
    val status = MutableLiveData<ProductSearchScreenStatus>()
    val sites = MutableLiveData<List<SiteModel>>()
    var indexSiteSelected = 0

    fun getSites() = viewModelScope.launch {
        status.value = ProductSearchScreenStatus.LOADING
        try {
            sites.value = api.getSites().sortedBy { it.name }
            indexSiteSelected = 0
            status.value = ProductSearchScreenStatus.DONE
        } catch (e: Exception) {
            status.value = ProductSearchScreenStatus.ERROR
            sites.value = listOf()
        }
    }
}

enum class ProductSearchScreenStatus { LOADING, ERROR, DONE }