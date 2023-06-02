package com.example.meli.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.meli.R
import com.example.meli.databinding.FragmentProductDetailsBinding
import com.example.meli.ui.viewmodels.ApiStatus
import com.example.meli.ui.viewmodels.ProductViewModel

class ProductDetailsFragment : Fragment() {
    private lateinit var binding: FragmentProductDetailsBinding
    private lateinit var viewModel: ProductViewModel
    val args: ProductDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        viewModel.getProduct(args.productId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(layoutInflater)
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
        viewModel.productSelected.observe(viewLifecycleOwner) {
            binding.productId.text = it?.title
        }

        return binding.root
    }
}