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

/**
 * Este fragment crea una vista que contiene detalles del producto. Los detalles que se muestran son:
 * una lista de imagenes, titulo, precio y contiene un botón para navegar a la web/app de MercadoLibre
 *
 * This fragment creates a view that contains product details. The details shown are:
 * a list of images, title, price and contains a button to navigate to the MercadoLibre website/app
 */
class ProductDetailsFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding
    private lateinit var viewModel: ProductDetailsViewModel
    private val args: ProductDetailsFragmentArgs by navArgs()

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

    /**
     * setStatusObserver() se encarga de manipular la visibilidad de objetos
     * dependiendo del estado de la pantalla
     *
     * setStatusObserver() is responsible for manipulating the visibility of objects
     * depending on the screen's state
     */
    private fun setStatusObserver() {
        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                ProductDetailsScreenStatus.LOADING -> {
                    binding.statusImage.visibility = View.VISIBLE
                    binding.statusImage.setImageResource(R.drawable.loading_animation)
                    binding.productImagePager.visibility = View.GONE
                    binding.productTitle.visibility = View.GONE
                    binding.productPrice.visibility = View.GONE
                    binding.showMore.visibility = View.GONE
                }
                ProductDetailsScreenStatus.ERROR -> {
                    binding.statusImage.visibility = View.VISIBLE
                    binding.statusImage.setImageResource(R.drawable.ic_connection_error)
                    binding.productImagePager.visibility = View.GONE
                    binding.productTitle.visibility = View.GONE
                    binding.productPrice.visibility = View.GONE
                    binding.showMore.visibility = View.GONE
                }
                ProductDetailsScreenStatus.DONE -> {
                    binding.statusImage.visibility = View.GONE
                    binding.productImagePager.visibility = View.VISIBLE
                    binding.productTitle.visibility = View.VISIBLE
                    binding.productPrice.visibility = View.VISIBLE
                    binding.showMore.visibility = View.VISIBLE
                }
                null -> {}
            }
        }
    }

    /**
     * initView() crea el adapter, setea los datos y comportamientos de pantalla
     *
     * initView() creates the adapter, set the screen data and behaviors
     */
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