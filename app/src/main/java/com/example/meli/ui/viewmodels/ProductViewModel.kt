package com.example.meli.ui.viewmodels

import androidx.lifecycle.ViewModel

class ProductViewModel : ViewModel() {
    private val placeholderImage = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/160px-Placeholder_view_vector.svg.png"
    val mockProductItems = listOf(
        ProductItem(imageUrl = placeholderImage, title = "Moto G72 Dual Sim 128 Gb Negro 6 Gb Ram", price = "USD 375"),
        ProductItem(imageUrl = placeholderImage, title = "Moto E20 Dual Sim 32 Gb Gris Grafito 2 Gb Ram", price = "USD 119"),
        ProductItem(imageUrl = placeholderImage, title = "Moto G31 Dual Sim 128 Gb Gris Meteoro 4 Gb Ram", price = "USD 199"),
        ProductItem(imageUrl = placeholderImage, title = "Moto G72 Dual Sim 128 Gb Negro 6 Gb Ram", price = "USD 375"),
        ProductItem(imageUrl = placeholderImage, title = "Moto E20 Dual Sim 32 Gb Gris Grafito 2 Gb Ram", price = "USD 119"),
        ProductItem(imageUrl = placeholderImage, title = "Moto G31 Dual Sim 128 Gb Gris Meteoro 4 Gb Ram", price = "USD 199"),
        ProductItem(imageUrl = placeholderImage, title = "Moto G72 Dual Sim 128 Gb Negro 6 Gb Ram", price = "USD 375"),
        ProductItem(imageUrl = placeholderImage, title = "Moto E20 Dual Sim 32 Gb Gris Grafito 2 Gb Ram", price = "USD 119"),
        ProductItem(imageUrl = placeholderImage, title = "Moto G31 Dual Sim 128 Gb Gris Meteoro 4 Gb Ram", price = "USD 199"),
        ProductItem(imageUrl = placeholderImage, title = "Moto G72 Dual Sim 128 Gb Negro 6 Gb Ram", price = "USD 375"),
        ProductItem(imageUrl = placeholderImage, title = "Moto E20 Dual Sim 32 Gb Gris Grafito 2 Gb Ram", price = "USD 119"),
        ProductItem(imageUrl = placeholderImage, title = "Moto G31 Dual Sim 128 Gb Gris Meteoro 4 Gb Ram", price = "USD 199")
    )
}

data class ProductItem(val imageUrl: String, val title: String, val price: String)