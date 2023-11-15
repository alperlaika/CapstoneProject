package com.laikaalper.florist_app.data.model.response

data class GetProductDetailResponse(
    val product: Product?,
    val status: Int?,
    val message: String?
)
