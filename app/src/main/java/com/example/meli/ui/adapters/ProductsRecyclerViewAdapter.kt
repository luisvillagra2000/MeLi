package com.example.meli.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.meli.databinding.ProductItemBinding
import com.example.meli.ui.viewmodels.ProductItem
import com.squareup.picasso.Picasso


class ProductsRecyclerViewAdapter(
    private val values: List<ProductItem>,
    private val onProductClick: (itemId: String) -> Unit
) : RecyclerView.Adapter<ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ProductItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(values[position], onProductClick)
    }

    override fun getItemCount(): Int = values.size
}

class ProductViewHolder(val binding: ProductItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ProductItem, onClick: (itemId: String) -> Unit) {
        Picasso.get().load(item.imageUrl).into(binding.productImage)
        binding.productTitle.text = item.title
        binding.productPrice.text = item.price
        binding.root.setOnClickListener { onClick(item.title) }
    }
}
