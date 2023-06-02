package com.example.meli.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.meli.R
import com.example.meli.databinding.FragmentProductsListBinding
import com.example.meli.ui.adapters.ProductsRecyclerViewAdapter
import com.example.meli.ui.viewmodels.ApiStatus
import com.example.meli.ui.viewmodels.ProductViewModel

class ProductListFragment : Fragment() {

    private lateinit var binding: FragmentProductsListBinding
    private lateinit var viewModel: ProductViewModel
    val args: ProductListFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        viewModel.getProductFiltered(args.ItemToSearch)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsListBinding.inflate(layoutInflater)
        val view = binding.root
        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                ApiStatus.LOADING -> {
                    binding.statusImage.visibility = View.VISIBLE
                    binding.statusImage.setImageResource(R.drawable.loading_animation)
                }

                ApiStatus.ERROR -> {
                    binding.statusImage.visibility = View.VISIBLE
                    binding.statusImage.setImageResource(R.drawable.ic_connection_error)
                }

                ApiStatus.DONE -> {
                    binding.statusImage.visibility = View.GONE
                }
            }
        }
        val adapter = ProductsRecyclerViewAdapter {
            findNavController().navigate(ProductListFragmentDirections.actionProductDetails(it))
        }
        viewModel.products.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        binding.productList.adapter = adapter


        return view
    }

}