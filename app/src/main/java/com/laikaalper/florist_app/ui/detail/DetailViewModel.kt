package com.laikaalper.florist_app.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laikaalper.florist_app.common.Resource
import com.laikaalper.florist_app.data.model.request.AddToCartRequest
import com.laikaalper.florist_app.data.model.response.BaseResponse
import com.laikaalper.florist_app.data.model.response.ProductUI
import com.laikaalper.florist_app.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {

    private var _detailState = MutableLiveData<DetailState>()
    val detailState: LiveData<DetailState> get() = _detailState

    private var _cartState = MutableLiveData<DetailState>()
    val cartState: LiveData<DetailState> get() = _cartState

    fun getProductDetail(id: Int) = viewModelScope.launch {
        _detailState.value = DetailState.Loading

        _detailState.value = when (val result = productRepository.getProductDetail(id)) {
            is Resource.Success -> DetailState.SuccessState(result.data)
            is Resource.Fail -> DetailState.EmptyScreen(result.failMessage)
            is Resource.Error -> DetailState.ShowPopUp(result.errorMessage)
        }
    }

    fun addCart(addToCartRequest: AddToCartRequest, isSuccess: ((Boolean) -> Unit)?) =
        viewModelScope.launch {
            _cartState.value = DetailState.Loading
            when (val request = productRepository.addToCart(addToCartRequest, isSuccess)) {
                is Resource.Success -> {
                    _cartState.value = DetailState.AddToCart(request.data)
                    isSuccess?.let { launch(Dispatchers.Main) { it(true) } }

                }

                is Resource.Error -> {
                    _cartState.value = DetailState.ShowPopUp(request.errorMessage)
                    isSuccess?.let { launch(Dispatchers.Main) { it(false) } }
                }

                else -> Unit

            }
        }
}

sealed interface DetailState {
    object Loading : DetailState
    data class SuccessState(val product: ProductUI) : DetailState
    data class EmptyScreen(val failMessage: String) : DetailState
    data class ShowPopUp(val errorMessage: String) : DetailState
    data class AddToCart(val baseResponse: BaseResponse) : DetailState
}