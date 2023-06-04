package com.example.meli.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.meli.R
import com.example.meli.databinding.FragmentProductDetailsBinding
import com.example.meli.ui.adapters.ProductImageAdapter
import com.example.meli.ui.viewmodels.ProductDetailsScreenStatus
import com.example.meli.ui.viewmodels.ProductDetailsViewModel
import com.google.android.material.tabs.TabLayoutMediator

class ProductDetailsFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding
    private lateinit var viewModel: ProductDetailsViewModel
    val args: ProductDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ProductDetailsViewModel::class.java]
        viewModel.getProduct(args.productId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(layoutInflater)
        setStatusObserver()
        initView()
        return binding.root
    }

    private fun setStatusObserver() {
        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                ProductDetailsScreenStatus.LOADING -> {
                    binding.statusImage.visibility = View.VISIBLE
                    binding.statusImage.setImageResource(R.drawable.loading_animation)
                    binding.showMore.visibility = View.GONE
                }

                ProductDetailsScreenStatus.ERROR -> {
                    binding.statusImage.visibility = View.VISIBLE
                    binding.statusImage.setImageResource(R.drawable.ic_connection_error)
                    binding.showMore.visibility = View.GONE
                }

                ProductDetailsScreenStatus.DONE -> {
                    binding.statusImage.visibility = View.GONE
                    binding.showMore.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initView() {
        val adapter = ProductImageAdapter()
        binding.productImagePager.adapter = adapter
        TabLayoutMediator(binding.pageIndicator, binding.productImagePager)
        { _, _ -> }.attach()
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getProduct(args.productId)
            binding.swipeRefresh.isRefreshing = false
        }
        viewModel.productSelected.observe(viewLifecycleOwner) { product ->
            product?.let {
                adapter.submitList(product.pictures)
                binding.productTitle.text = product.title
                binding.productPrice.text = product.currency_id + product.price
                binding.showMore.setOnClickListener {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(product.permalink)))
                }
            }
        }
    }
}