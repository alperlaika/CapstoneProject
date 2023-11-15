package com.laikaalper.florist_app.data.model.response

data class GetProductsResponse(
    val products: List<Product>?,
    val status: Int?,
    val message: String?
)
