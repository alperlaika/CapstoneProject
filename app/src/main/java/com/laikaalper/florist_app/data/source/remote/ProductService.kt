package com.laikaalper.florist_app.data.source.remote

import com.laikaalper.florist_app.data.model.request.AddToCartRequest
import com.laikaalper.florist_app.data.model.request.ClearCartRequest
import com.laikaalper.florist_app.data.model.request.DeleteFromCartRequest
import com.laikaalper.florist_app.data.model.response.BaseResponse
import com.laikaalper.florist_app.data.model.response.GetCartProductsResponse
import com.laikaalper.florist_app.data.model.response.GetProductDetailResponse
import com.laikaalper.florist_app.data.model.response.GetProductsResponse
import com.laikaalper.florist_app.data.model.response.GetSaleProductsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ProductService {

    @GET("get_products.php")
    suspend fun getProducts(): Response<GetProductsResponse>

    @GET("get_sale_products.php")
    suspend fun getSaleProducts(): Response<GetSaleProductsResponse>

    @GET("get_product_detail.php")
    suspend fun getProductDetail(
        @Query("id") id: Int
    ): Response<GetProductDetailResponse>

    @GET("search_product.php")
    suspend fun getSearchProduct(@Query("query") queryValue: String): GetProductsResponse

    @GET("get_cart_products.php")
    suspend fun getCartProducts(
        @Query("userId") userId: String
    ): Response<GetCartProductsResponse>

    @POST("add_to_cart.php")
    suspend fun addToCart(
        @Body addToCartRequest: AddToCartRequest
    ): BaseResponse

    @POST("delete_from_cart.php")
    suspend fun deleteFromCart(
        @Body deleteFromCartRequest: DeleteFromCartRequest
    ): BaseResponse

    @POST("clear_cart.php")
    suspend fun clearCart(
        @Body clearCartRequest: ClearCartRequest
    ): BaseResponse
}