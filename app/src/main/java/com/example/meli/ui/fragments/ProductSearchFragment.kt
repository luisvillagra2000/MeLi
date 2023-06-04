package com.example.meli.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.meli.R
import com.example.meli.databinding.FragmentProductSearchBinding
import com.example.meli.ui.viewmodels.ProductSearchScreenStatus
import com.example.meli.ui.viewmodels.ProductSearchViewModel

class ProductSearchFragment : Fragment() {

    private lateinit var binding: FragmentProductSearchBinding
    private lateinit var viewModel: ProductSearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ProductSearchViewModel::class.java]
        viewModel.getSites()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductSearchBinding.inflate(layoutInflater)
        setStatusObserver()
        initView()
        return binding.root
    }

    override fun onDestroyView() {
        viewModel.indexSiteSelected = binding.sitesSpinner.selectedItemPosition
        super.onDestroyView()
    }

    private fun initView() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getSites()
            binding.swipeRefresh.isRefreshing = false
        }
        viewModel.sites.observe(viewLifecycleOwner) { sites ->
            val sitesNames = sites.map { it.name }
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sitesNames)
            binding.sitesSpinner.adapter = adapter
            binding.sitesSpinner.setSelection(viewModel.indexSiteSelected)
        }
        binding.searchButton.setOnClickListener {
            val search = binding.searchText.text.toString()
            if (validateSearch(search)) {
                val siteSelected = viewModel.sites.value?.find { it.name == binding.sitesSpinner.selectedItem.toString() }?.id!!
                findNavController().navigate(
                    ProductSearchFragmentDirections.actionProductsList(
                        sanitizeInput(search),
                        siteSelected
                    )
                )
            }
        }
    }

    private fun setStatusObserver() {
        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                ProductSearchScreenStatus.LOADING -> {
                    binding.statusImage.visibility = View.VISIBLE
                    binding.statusImage.setImageResource(R.drawable.loading_animation)
                    binding.sitesLayout.visibility = View.GONE
                    binding.searchText.visibility = View.GONE
                    binding.searchButton.visibility = View.GONE
                }
                ProductSearchScreenStatus.ERROR -> {
                    binding.statusImage.visibility = View.VISIBLE
                    binding.statusImage.setImageResource(R.drawable.ic_connection_error)
                    binding.sitesLayout.visibility = View.GONE
                    binding.searchText.visibility = View.GONE
                    binding.searchButton.visibility = View.GONE
                }
                ProductSearchScreenStatus.DONE -> {
                    binding.statusImage.visibility = View.GONE
                    binding.sitesLayout.visibility = View.VISIBLE
                    binding.searchText.visibility = View.VISIBLE
                    binding.searchButton.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun validateSearch(search: String?) = if (search.isNullOrBlank()) {
        Toast.makeText(context, R.string.error_text_blank, Toast.LENGTH_SHORT).show()
        false
    } else if (binding.sitesSpinner.selectedItem.toString().isBlank()) {
        Toast.makeText(context, R.string.error_country_blank, Toast.LENGTH_SHORT).show()
        false
    } else {
        true
    }

    private fun sanitizeInput(input: String) =
        input.replace("'", "")
            .replace("\"", "")
            .replace("?", "")
            .replace("&", "")
            .replace("-", "")
            .replace(":", "")
            .replace(";", "")
            .replace("\\", "")
            .replace("|", "")
            .replace("/", "")
            .replace("+", "")
            .replace("*", "")
}