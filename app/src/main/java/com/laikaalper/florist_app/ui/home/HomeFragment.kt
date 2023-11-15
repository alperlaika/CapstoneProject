package com.laikaalper.florist_app.ui.home

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.laikaalper.florist_app.R
import com.laikaalper.florist_app.common.gone
import com.laikaalper.florist_app.common.viewBinding
import com.laikaalper.florist_app.common.visible
import com.laikaalper.florist_app.data.model.response.ProductUI
import com.laikaalper.florist_app.databinding.FragmentHomeBinding
import com.laikaalper.florist_app.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)

    private val viewModel by viewModels<HomeViewModel>()

    private val productAdapter =
        ProductsAdapter(onProductClick = ::onProductClick, onFavClick = ::onFavClick)
    private val saleAdapter =
        SaleAdapter(onProductClick = ::onProductClick, onFavClick = ::onFavClick)
    private val auth = FirebaseAuth.getInstance()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getProducts()
        viewModel.getSaleProducts()
        with(binding) {
            rvProducts.adapter = productAdapter
            binding.rvProducts.layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            binding.rvProducts.addItemDecoration(GenericDecoration(0, 20, 0, 0))

            rvSale.adapter = saleAdapter
            binding.rvProducts.layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            binding.rvSale.addItemDecoration(GenericDecoration(0, 20, 0, 0))

        }
        observeData()
        logOut()
    }


    private fun observeData() = with(binding) {

        viewModel.homeState.observe(viewLifecycleOwner) { state ->
            when (state) {
                HomeState.Loading -> progressBar.visible()

                is HomeState.SuccessState -> {
                    progressBar.gone()
                    productAdapter.submitList(state.products)
                }

                is HomeState.SuccessSaleState -> {
                    progressBar.gone()
                    saleAdapter.submitList(state.products)

                }

                is HomeState.EmptyScreen -> {
                    progressBar.gone()
                    ivEmpty.visible()
                    tvEmpty.visible()
                    tvEmpty.text = state.failMessage
                }

                is HomeState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }

                is HomeState.SuccessSaleState -> {
                    progressBar.gone()
                    saleAdapter.submitList(state.products)
                    println(state.products)
                }
            }
        }

    }

    private fun onProductClick(id: Int) {
        val bundle = Bundle()
        bundle.putInt("id",id)
        MainActivity.navigate(R.id.push_detail_fragment,bundle)
    }

    private fun onFavClick(product: ProductUI) {
        viewModel.setFavoriteState(product)
    }

    private fun logOut() {
        binding.logOut.setOnClickListener {
            auth.signOut()
            MainActivity.navigate(R.id.push_sign_in_fragment)
        }
    }

}

class GenericDecoration(left: Int, right: Int, top: Int, bottom: Int) :
    RecyclerView.ItemDecoration() {

    private var left = 0
    private var right = 0
    private var top = 0
    private var bottom = 0

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect[left, top, right] = bottom
    }

    init {
        this.left = left
        this.right = right
        this.top = top
        this.bottom = bottom
    }
}