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
import com.example.meli.ui.viewmodels.ProductsListScreenStatus
import com.example.meli.ui.viewmodels.ProductsListViewModel

class ProductsListFragment : Fragment() {

    private lateinit var binding: FragmentProductsListBinding
    private lateinit var viewModel: ProductsListViewModel
    private val args: ProductsListFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ProductsListViewModel::class.java]
        viewModel.getProductFiltered(args.siteId, args.itemToSearch)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsListBinding.inflate(layoutInflater)
        setStatusObserver()
        initView()
        return binding.root
    }

    private fun setStatusObserver() {
        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                ProductsListScreenStatus.LOADING -> {
                    binding.statusImage.visibility = View.VISIBLE
                    binding.statusImage.setImageResource(R.drawable.loading_animation)
                    binding.productList.visibility = View.GONE
                }
                ProductsListScreenStatus.ERROR -> {
                    binding.statusImage.visibility = View.VISIBLE
                    binding.statusImage.setImageResource(R.drawable.ic_connection_error)
                    binding.productList.visibility = View.GONE
                }
                ProductsListScreenStatus.DONE -> {
                    binding.statusImage.visibility = View.GONE
                    binding.productList.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initView() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getProductFiltered(args.siteId, args.itemToSearch)
            binding.swipeRefresh.isRefreshing = false
        }
        val adapter = ProductsRecyclerViewAdapter {
            findNavController().navigate(ProductsListFragmentDirections.actionProductDetails(it))
        }
        binding.productList.adapter = adapter
        viewModel.products.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}