package com.laikaalper.florist_app.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.laikaalper.florist_app.MainApplication
import com.laikaalper.florist_app.data.model.response.ProductUI
import com.laikaalper.florist_app.databinding.ItemCartBinding
import com.laikaalper.florist_app.di.RepositoryModule
import dagger.hilt.EntryPoints


class CartProductsAdapter(
    private val onProductClick: (Int) -> Unit,
    private val onCartDeleteClick: (Int, String) -> Unit
) : ListAdapter<ProductUI, CartProductsAdapter.CartProductViewHolder>(ProductDiffUtilCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
        return CartProductViewHolder(
            ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onProductClick,
            onCartDeleteClick
        )
    }

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) =
        holder.bind(getItem(position))


    class CartProductViewHolder(
        private val binding: ItemCartBinding,
        private val onProductClick: (Int) -> Unit,
        private val onCartDeleteClick: (Int, String) -> Unit

    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUI) {
            with(binding) {
                tvTitle.text = product.title
                tvPrice.text = "${product.price} ₺"
                Glide.with(ivProduct).load(product.imageOne).into(ivProduct)
                if (product.saleState) {
                    tvPrice.text = "${product.salePrice} ₺"
                } else {

                    tvPrice.text = "${product.price} ₺"

                }
                ivDelete.setOnClickListener {
                    val auth = EntryPoints.get(
                        MainApplication.context,
                        RepositoryModule.RepositoryModuleInterface::class.java
                    ).getAuthRepo()
                    val userId = auth.getUserId()
                    onCartDeleteClick(product.id, userId)
                }
                root.setOnClickListener {
                    onProductClick(product.id)

                }

            }
        }
    }

    class ProductDiffUtilCallBack : DiffUtil.ItemCallback<ProductUI>() {
        override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem == newItem
        }
    }
}