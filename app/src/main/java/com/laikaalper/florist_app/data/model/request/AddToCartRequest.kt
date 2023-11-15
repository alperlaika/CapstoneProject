package com.laikaalper.florist_app.data.model.request

data class AddToCartRequest(
    val userId: String,
    val productId: Int
)
