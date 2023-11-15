package com.laikaalper.florist_app.ui.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.laikaalper.florist_app.R
import com.laikaalper.florist_app.common.gone
import com.laikaalper.florist_app.common.viewBinding
import com.laikaalper.florist_app.common.visible
import com.laikaalper.florist_app.data.model.response.ProductUI
import com.laikaalper.florist_app.databinding.FragmentCartBinding
import com.laikaalper.florist_app.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {
    private val binding by viewBinding(FragmentCartBinding::bind)
    private val viewModel by viewModels<CartViewModel>()
    private val cartProductsAdapter = CartProductsAdapter(
        onProductClick = ::onProductClick,
        onCartDeleteClick = ::onCartDeleteClick
    )

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid.toString()

        viewModel.getCartProducts(userId.toString())
        viewModel.totalPrice.observe(viewLifecycleOwner) { totalPrice ->
            binding.tvTotalPrice.text = getString(R.string.total_price, totalPrice)

        }

        with(binding) {
            rvCart.adapter = cartProductsAdapter

            btnClear.setOnClickListener {
                viewModel.clearCart(userId)
                viewModel.getCartProducts(userId)
            }
            btnBuy.setOnClickListener {
                MainActivity.navigate(R.id.push_payment_fragment)
            }

        }
        observeData()
    }


    private fun observeData() = with(binding) {
        viewModel.cartState.observe(viewLifecycleOwner) { state ->
            when (state) {
                CartState.Loading -> progressBar.visible()

                is CartState.SuccessState -> {
                    progressBar.gone()
                    cartProductsAdapter.submitList(state.products)


                }

                is CartState.EmptyScreen -> {

                    progressBar.gone()
                    ivEmpty.visible()
                    tvEmpty.visible()
                    tvEmpty.text = state.failMessage
                    val list = ArrayList<ProductUI>()
                    cartProductsAdapter.submitList(list)
                    viewModel.setTotalPrice(0.0)

                }

                is CartState.DeleteFromCart -> {
                    Snackbar.make(requireView(), state.baseResponse.message.toString(), 1000).show()
                }

                is CartState.ClearCart -> {
                    Snackbar.make(requireView(), state.baseResponse.message.toString(), 1000).show()

                }

                is CartState.ShowPopUp -> {
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

    private fun onCartDeleteClick(id: Int, userId: String) {
        viewModel.deleteFromCart(id, userId)
        viewModel.getCartProducts(userId)
    }
}
