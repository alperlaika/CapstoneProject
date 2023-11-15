package com.laikaalper.florist_app.ui.search

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.laikaalper.florist_app.common.Resource
import com.laikaalper.florist_app.data.model.response.ProductUI
import com.laikaalper.florist_app.data.repository.ProductRepository
import com.laikaalper.florist_app.ui.viewmodeel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val productRepository: ProductRepository, application: Application
) : BaseViewModel(application) {

    private var _searchState = MutableLiveData<SearchState>()
    val searchState: LiveData<SearchState>
        get() = _searchState

    fun getSearchProduct(query: String) {
        launch {
            _searchState.value = SearchState.Loading
            val result = productRepository.getSearchProduct(query)
            when (result) {
                is Resource.Success -> {
                    _searchState.value = SearchState.Data(result.data)
                }

                is Resource.Error -> {
                    _searchState.value = SearchState.Error(result.errorMessage)
                }

                else -> Unit

            }

        }
    }
}

sealed interface SearchState {
    object Loading : SearchState
    data class Data(val products: List<ProductUI>) : SearchState
    data class Error(val errorMessage: String) : SearchState
}