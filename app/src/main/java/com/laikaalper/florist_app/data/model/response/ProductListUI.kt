package com.laikaalper.florist_app.data.model.response

data class ProductListUI(
    val id: Int,
    val title: String,
    val price: Double,
    val imageOne: String,
    val isFav: Boolean
)