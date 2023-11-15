package com.laikaalper.florist_app.ui.favorites

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.laikaalper.florist_app.R
import com.laikaalper.florist_app.common.gone
import com.laikaalper.florist_app.common.viewBinding
import com.laikaalper.florist_app.common.visible
import com.laikaalper.florist_app.data.model.response.ProductUI
import com.laikaalper.florist_app.databinding.FragmentFavoritesBinding
import com.laikaalper.florist_app.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private val binding by viewBinding(FragmentFavoritesBinding::bind)

    private val viewModel by viewModels<FavoritesViewModel>()

    private val productAdapter =
        FavoritesAdapter(onProductClick = ::onProductClick, onDeleteClick = ::onDeleteClick)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFavorites()

        with(binding) {
            rvFavProducts.adapter = productAdapter
        }

        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.favoritesState.observe(viewLifecycleOwner) { state ->
            when (state) {
                FavoritesState.Loading -> progressBar.visible()

                is FavoritesState.SuccessState -> {
                    progressBar.gone()
                    productAdapter.submitList(state.products)
                }

                is FavoritesState.EmptyScreen -> {
                    progressBar.gone()
                    ivEmpty.visible()
                    tvEmpty.visible()
                    rvFavProducts.gone()
                    tvEmpty.text = state.failMessage
                }

                is FavoritesState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }

    private fun onProductClick(id: Int) {
        val bundle = Bundle()
        bundle.putInt("id",id)
        MainActivity.navigate(R.id.push_detail_fragment,bundle)
    }

    private fun onDeleteClick(product: ProductUI) {
        viewModel.deleteFromFavorites(product)
    }
}