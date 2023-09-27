package com.example.meli.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.meli.R
import com.example.meli.databinding.FragmentProductSearchBinding
import com.example.meli.ui.viewmodels.ProductSearchScreenStatus
import com.example.meli.ui.viewmodels.ProductSearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Este fragment se encarga de obtener la información necesaria para realizar una búsqueda de productos.
 * Solicita un valor para la búsqueda y en qué país desea realizar esa búsqueda
 *
 * This fragment is responsible for obtaining the necessary information to perform a product search.
 * Request a value for the search and in which country you want to perform that search
 */
class ProductSearchFragment : Fragment() {

    private lateinit var binding: FragmentProductSearchBinding
    private val viewModel: ProductSearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        // Guardo el index del sitio seleccionado antes de que se destruya la vista
        // para no perder la selección del usuario en caso de que la pantalla rote

        // I save the index of the selected site before the view be destroyed
        // to don't lose the user's selection in the case that the screen rotates
        viewModel.indexSiteSelected = binding.sitesSpinner.selectedItemPosition
        super.onDestroyView()
    }

    /**
     * initView() crea el adapter, setea los datos y comportamientos de pantalla
     *
     * initView() creates the adapter, set the screen data and behaviors
     */

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
            if (validateSearch()) {
                val siteSelected = viewModel.sites.value?.find { it.name == binding.sitesSpinner.selectedItem.toString() }?.id!!
                findNavController().navigate(
                    ProductSearchFragmentDirections.actionProductsList(
                        sanitizeInput(binding.searchText.text.toString()),
                        siteSelected
                    )
                )
            }
        }
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
                null -> {}
            }
        }
    }

    /**
     * validateSearch() valida que los campos necesarios para la búsqueda no estén vacíos
     * y en caso de que lo estén muestra un mensaje al usuario
     *
     * validateSearch() validates that the necessary fields for the search are not empty and
     * in case there are empty the funtion will show a message to the user
     */
    private fun validateSearch() = if (binding.searchText.text.toString().isBlank()) {
        Toast.makeText(context, R.string.error_text_blank, Toast.LENGTH_SHORT).show()
        false
    } else if (binding.sitesSpinner.selectedItem.toString().isBlank()) {
        Toast.makeText(context, R.string.error_country_blank, Toast.LENGTH_SHORT).show()
        false
    } else {
        true
    }

    /**
     * sanitizeInput() elimina caracteres especiales que pueden ser usados con malicia
     * o puedan causar problemas
     *
     * sanitizeInput() removes special characters that can be used with malice
     * or can cause problems
     */
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