package com.example.meli.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.meli.databinding.FragmentProductSearchBinding
import com.example.meli.ui.viewmodels.ProductViewModel

class ProductSearchFragment : Fragment() {

    private lateinit var binding: FragmentProductSearchBinding
    private lateinit var viewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductSearchBinding.inflate(layoutInflater)
        binding.searchBtn.setOnClickListener {
            val search = binding.searchText.text.toString()
            if (validateSearch(search))
                findNavController().navigate(
                    ProductSearchFragmentDirections.actionProductsList(
                        search
                    )
                )
        }
        return binding.root
    }

    private fun validateSearch(search: String): Boolean {
        return true
    }
}