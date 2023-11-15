package com.laikaalper.florist_app.data.repository

import com.laikaalper.florist_app.common.Resource
import com.laikaalper.florist_app.data.mapper.mapProductEntityToProductUI
import com.laikaalper.florist_app.data.mapper.mapProductToProductUI
import com.laikaalper.florist_app.data.mapper.mapToProductEntity
import com.laikaalper.florist_app.data.mapper.mapToProductUI
import com.laikaalper.florist_app.data.model.request.AddToCartRequest
import com.laikaalper.florist_app.data.model.request.ClearCartRequest
import com.laikaalper.florist_app.data.model.request.DeleteFromCartRequest
import com.laikaalper.florist_app.data.model.response.BaseResponse
import com.laikaalper.florist_app.data.model.response.ProductUI
import com.laikaalper.florist_app.data.source.local.ProductDao
import com.laikaalper.florist_app.data.source.remote.ProductService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductRepository(
    private val productService: ProductService,
    private val productDao: ProductDao
) {

    suspend fun getProducts(): Resource<List<ProductUI>> =
        withContext(Dispatchers.IO) {
            try {
                val favorites = productDao.getProductIds()
                val response = productService.getProducts().body()

                if (response?.status == 200) {
                    Resource.Success(response.products.orEmpty().mapProductToProductUI(favorites))
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }

    suspend fun getSaleProducts(): Resource<List<ProductUI>> =
        withContext(Dispatchers.IO) {
            try {
                val favorites = productDao.getProductIds()
                val response = productService.getSaleProducts().body()

                if (response?.status == 200) {
                    Resource.Success(response.products.orEmpty().mapProductToProductUI(favorites))
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }

    suspend fun getProductDetail(id: Int): Resource<ProductUI> =
        withContext(Dispatchers.IO) {
            try {
                val favorites = productDao.getProductIds()
                val response = productService.getProductDetail(id).body()

                if (response?.status == 200 && response.product != null) {
                    Resource.Success(response.product.mapToProductUI(favorites))
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }

    suspend fun addToFavorites(productUI: ProductUI) {
        productDao.addProduct(productUI.mapToProductEntity())
    }

    suspend fun deleteFromFavorites(productUI: ProductUI) {
        productDao.deleteProduct(productUI.mapToProductEntity())
    }

    suspend fun getFavorites(): Resource<List<ProductUI>> =
        withContext(Dispatchers.IO) {
            try {
                val products = productDao.getProducts()

                if (products.isEmpty()) {
                    Resource.Fail("Ürünleriniz Bulunamadı")
                } else {
                    Resource.Success(products.mapProductEntityToProductUI())
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }

    suspend fun getSearchProduct(query: String): Resource<List<ProductUI>> {
        return try {
            val favorites = productDao.getProductIds()
            val response = productService.getSearchProduct(query)
            Resource.Success(response.products?.map { it.mapToProductUI(favorites) }.orEmpty())
        } catch (e: Exception) {
            Resource.Error(e.message.orEmpty())
        }
    }

    suspend fun getCartProducts(userId: String): Resource<List<ProductUI>> =
        withContext(Dispatchers.IO) {
            try {
                val response = productService.getCartProducts(userId).body()
                val favorites = productDao.getProductIds()

                if (response?.status == 200) {
                    Resource.Success(response.products.orEmpty().mapProductToProductUI(favorites))
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Resource.Error("Bazı Şeyler Yolunda Gitmedi. En Kısa Zamanda Düzelteceğiz")
            }
        }

    suspend fun deleteFromCart(deleteFromCartRequest: DeleteFromCartRequest): Resource<BaseResponse> =

        withContext(Dispatchers.IO) {
            try {
                val result = productService.deleteFromCart(deleteFromCartRequest)

                if (result.status == 200) {
                    Resource.Success(result)
                } else {
                    Resource.Fail("Bazı Şeyler Yolunda Gitmedi!")
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }

    suspend fun clearCart(clearCartRequest: ClearCartRequest): Resource<BaseResponse> =
        withContext(Dispatchers.IO) {
            try {
                val result = productService.clearCart(clearCartRequest)

                if (result.status == 200) {
                    Resource.Success(result)
                } else {
                    Resource.Error("Bazı Şeyler Yolunda Gitmedi!")
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }

    suspend fun addToCart(
        addToCartRequest: AddToCartRequest,
        isSuccess: ((Boolean) -> Unit)?
    ): Resource<BaseResponse> =
        withContext(Dispatchers.IO) {
            try {

                val result = productService.addToCart(addToCartRequest)
                if (result.status == 200) {
                    isSuccess?.let { launch(Dispatchers.Main) { it(true) } }
                    Resource.Success(result)

                } else {
                    isSuccess?.let { launch(Dispatchers.Main) { it(false) } }

                    Resource.Fail("Bazı Şeyler Yolunda Gitmedi!")
                }

            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }

}