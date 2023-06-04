package com.example.meli.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.meli.R
import com.example.meli.data.models.Pictures
import com.example.meli.databinding.ProductImageItemBinding

class ProductImageAdapter : ListAdapter<Pictures, ProductImageViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductImageViewHolder(
        ProductImageItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ProductImageViewHolder, position: Int) =
        holder.bind(getItem(position).url)

    companion object DiffCallback : DiffUtil.ItemCallback<Pictures>() {
        override fun areItemsTheSame(oldItem: Pictures, newItem: Pictures) =
            oldItem.url == newItem.url

        override fun areContentsTheSame(oldItem: Pictures, newItem: Pictures) =
            oldItem.url == newItem.url
    }
}

class ProductImageViewHolder(private val binding: ProductImageItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(imageUrlString: String) {
        val imageUri = imageUrlString.replace("http://", "https://").toUri()
        binding.root.load(imageUri) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
    }
}
