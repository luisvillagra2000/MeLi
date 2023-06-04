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

