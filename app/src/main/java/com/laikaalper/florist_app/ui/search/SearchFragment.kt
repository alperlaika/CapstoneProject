package com.laikaalper.florist_app.ui.search

import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.laikaalper.florist_app.R
import com.laikaalper.florist_app.common.gone
import com.laikaalper.florist_app.common.viewBinding
import com.laikaalper.florist_app.common.visible
import com.laikaalper.florist_app.databinding.FragmentSearchBinding
import com.laikaalper.florist_app.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search), SearchAdapter.SearchProductListener {

    private val binding by viewBinding(FragmentSearchBinding::bind)

    private val viewModel by viewModels<SearchViewModel>()

    private val searchAdapter by lazy { SearchAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            rvSearch.adapter = searchAdapter

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let { query ->
                        if (query.length >= 3) {
                            viewModel.getSearchProduct(query)
                        } else {
                            Snackbar.make(
                                requireView(),
                                "Aramak istediğiniz ürün için en az 3 karakter giriniz!",
                                3000
                            ).show()
                        }
                    }
                    return true
                }
            })
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                MainActivity.navigate(R.id.push_detail_fragment)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.searchState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchState.Loading -> {
                    progressBar.visible()
                }

                is SearchState.Data -> {
                    progressBar.gone()
                    searchAdapter.submitList(state.products)
                }

                is SearchState.Error -> {
                    progressBar.gone()
                }
            }
        }
    }

    override fun onProductClick(id: Int) {
        val bundle = Bundle()
        bundle.putInt("id",id)
        MainActivity.navigate(R.id.push_detail_fragment,bundle)
    }
}