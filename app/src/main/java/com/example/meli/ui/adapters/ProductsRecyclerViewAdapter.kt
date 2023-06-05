package com.example.meli.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import coil.load
import com.example.meli.R
import com.example.meli.data.models.ProductModel
import com.example.meli.databinding.ProductItemBinding

/**
 * Este es el adapter es responsable de crear ítems que muestran
 * una imagen (obtenida desde una URL), título, y precio de un producto
 *
 * This is the Adapter is responsible for creating items that show
 * an image (obtained from a URL), title, and product of a product
 */
class ProductsRecyclerViewAdapter(private val onProductClick: (itemId: String) -> Unit) :
    ListAdapter<ProductModel, ProductViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ProductItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bind(getItem(position), onProductClick)

    companion object DiffCallback : DiffUtil.ItemCallback<ProductModel>() {
        override fun areItemsTheSame(oldItem: ProductModel, newItem: ProductModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ProductModel, newItem: ProductModel) =
            oldItem.permalink == newItem.permalink
    }
}

class ProductViewHolder(private val binding: ProductItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ProductModel, onClick: (itemId: String) -> Unit) {
        //Los links que empiezan con http no están funcionando, cree un parche
        // (cambiando http por https) para poder mostrar las imagenes

        //the links that start with http are not working, create a patch
        // (changing http for https) to show the images
        val imageUri = item.thumbnail.replace("http://", "https://").toUri()
        binding.productImage.load(imageUri) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
        binding.productTitle.text = item.title
        binding.productPrice.text = item.currency_id + item.price
        binding.root.setOnClickListener { onClick(item.id) }
    }
}

