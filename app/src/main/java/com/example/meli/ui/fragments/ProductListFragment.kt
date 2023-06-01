package com.example.meli.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.meli.databinding.FragmentProductsListBinding
import com.example.meli.ui.adapters.ProductsRecyclerViewAdapter
import com.example.meli.ui.viewmodels.ProductViewModel

class ProductListFragment : Fragment() {

    companion object {
        const val COLUMN = 2
    }

    private lateinit var binding: FragmentProductsListBinding
    private lateinit var viewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsListBinding.inflate(layoutInflater)
        val view = binding.root

        with(view) {
            layoutManager = GridLayoutManager(context, COLUMN)
            adapter = ProductsRecyclerViewAdapter(viewModel.mockProductItems) {
                findNavController().navigate(ProductListFragmentDirections.actionProductDetails(it))
            }
        }

        return view
    }

}