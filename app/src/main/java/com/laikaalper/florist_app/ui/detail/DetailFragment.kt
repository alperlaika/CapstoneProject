package com.laikaalper.florist_app.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.laikaalper.florist_app.MainApplication
import com.laikaalper.florist_app.R
import com.laikaalper.florist_app.common.gone
import com.laikaalper.florist_app.common.viewBinding
import com.laikaalper.florist_app.common.visible
import com.laikaalper.florist_app.data.model.request.AddToCartRequest
import com.laikaalper.florist_app.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val binding by viewBinding(FragmentDetailBinding::bind)

    private val viewModel by viewModels<DetailViewModel>()


    private lateinit var firebaseAuth: FirebaseAuth
    var id :Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid

        arguments?.let {
            if (it.containsKey("id")) {
                id = it.getInt("id")
                viewModel.getProductDetail(id ?: 0)
            }
        }



        observeData()

        with(binding) {

            btnAddCart.setOnClickListener {
                viewModel.addCart(AddToCartRequest(userId.toString(), id ?:0)) { success ->
                    if (success) {
                        progressBar.gone()
                        Snackbar.make(
                            requireActivity().findViewById(android.R.id.content),
                            "Ürün eklendi.",
                            1000
                        ).show()
                    } else {
                        Snackbar.make(
                            requireActivity().findViewById(android.R.id.content),
                            "Ürün eklenemedi yada zaten sepetinizde mevcut.",
                            1000
                        ).show()
                        progressBar.gone()
                    }
                }

            }
            ivBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }

    }

    private fun observeData() = with(binding) {
        viewModel.detailState.observe(viewLifecycleOwner) { state ->
            when (state) {
                DetailState.Loading -> progressBar.visible()

                is DetailState.SuccessState -> {
                    progressBar.gone()
                    Glide.with(ivProduct).load(state.product.imageOne).into(ivProduct)
                    tvTitle.text = state.product.title
                    tvSalePrice.text = "${state.product.salePrice} ₺"
                    tvDescription.text = state.product.description
                }

                is DetailState.EmptyScreen -> {
                    progressBar.gone()
                    ivEmpty.visible()
                    tvEmpty.visible()
                    tvEmpty.text = state.failMessage
                }

                is DetailState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }

                is DetailState.AddToCart -> {
                    Snackbar.make(requireView(), state.baseResponse.message.toString(), 1000).show()
                }
            }
        }
        viewModel.cartState.observe(viewLifecycleOwner) { state ->
            when (state) {
                DetailState.Loading -> progressBar.visible()

                is DetailState.SuccessState -> {
                    progressBar.gone()

                }

                else -> Unit
            }

        }

    }
}