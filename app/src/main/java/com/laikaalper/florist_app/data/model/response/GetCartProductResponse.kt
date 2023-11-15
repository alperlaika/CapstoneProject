package com.laikaalper.florist_app.data.model.response

data class GetCartProductsResponse(
    val products: List<Product>?,
) : BaseResponse()
