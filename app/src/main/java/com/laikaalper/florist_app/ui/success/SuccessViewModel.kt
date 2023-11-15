package com.laikaalper.florist_app.ui.success

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laikaalper.florist_app.common.Resource
import com.laikaalper.florist_app.data.model.request.ClearCartRequest
import com.laikaalper.florist_app.data.model.response.BaseResponse
import com.laikaalper.florist_app.data.model.response.ProductUI
import com.laikaalper.florist_app.data.repository.AuthRepository
import com.laikaalper.florist_app.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SuccessViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private var _successState = MutableLiveData<SucccessPageState>()

    val successState: LiveData<SucccessPageState> get() = _successState


    fun clearCart(userId: String) = viewModelScope.launch {
        val clearCartRequest = ClearCartRequest(userId)
        val request = productRepository.clearCart(clearCartRequest)
        when (request) {

            is Resource.Success -> {
                SucccessPageState.ClearCart(request.data)

                getCartProducts(authRepository.getUserId())
            }

            is Resource.Fail -> SucccessPageState.EmptyScreen(request.failMessage)
            is Resource.Error -> SucccessPageState.ShowPopUp(request.errorMessage)

        }
    }

    fun getCartProducts(userId: String) = viewModelScope.launch {
        _successState.value = SucccessPageState.Loading
        val response = productRepository.getCartProducts(userId)
        _successState.value = when (response) {

            is Resource.Success -> {
                SucccessPageState.SuccessState(response.data)

            }

            is Resource.Fail -> SucccessPageState.EmptyScreen(response.failMessage)
            is Resource.Error -> SucccessPageState.ShowPopUp(response.errorMessage)


        }
    }


}


sealed interface SucccessPageState {
    data object Loading : SucccessPageState
    data class SuccessState(val products: List<ProductUI>) : SucccessPageState
    data class ClearCart(val baseResponse: BaseResponse) : SucccessPageState
    data class EmptyScreen(val failMessage: String) : SucccessPageState
    data class ShowPopUp(val errorMessage: String) : SucccessPageState
}