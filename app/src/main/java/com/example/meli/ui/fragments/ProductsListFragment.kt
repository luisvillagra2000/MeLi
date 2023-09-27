package com.example.meli.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.meli.R
import com.example.meli.databinding.FragmentProductsListBinding
import com.example.meli.ui.adapters.ProductsRecyclerViewAdapter
import com.example.meli.ui.viewmodels.ProductsListScreenStatus
import com.example.meli.ui.viewmodels.ProductsListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Este fragment se encarga de mostrar la lista de los resultados de una búsqueda
 * en productos de MercadoLibre de país
 *
 * This fragment is responsible for showing the list of the results of
 * a MercadoLibre's products of a country
 */
class ProductsListFragment : Fragment() {

    private lateinit var binding: FragmentProductsListBinding
    private val viewModel: ProductsListViewModel by viewModel()
    private val args: ProductsListFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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