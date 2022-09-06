package com.vladimir.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import com.vladimir.shoppinglist.R
import com.vladimir.shoppinglist.databinding.ItemShopDisabledBinding
import com.vladimir.shoppinglist.databinding.ItemShopEnabledBinding
import com.vladimir.shoppinglist.domain.ShopItem

class ShopItemAdapter : ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback()) {

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null

    var onShopItemShortClickListener: ((ShopItem) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layout = when (viewType) {
            LIST_ITEM_IS_ACTIVE -> R.layout.item_shop_enabled
            LIST_ITEM_NOT_ACTIVE -> R.layout.item_shop_disabled
            else -> throw RuntimeException("Unknown viewType $viewType")
        }
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layout,
            parent,
            false
        )
        return ShopItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = getItem(position)
        val binding = holder.binding
        binding.root.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
        binding.root.setOnClickListener {
            onShopItemShortClickListener?.invoke(shopItem)
        }
        when(binding) {
            is ItemShopEnabledBinding -> {
                binding.shopItem = shopItem
            }
            is ItemShopDisabledBinding -> {
                binding.shopItem = shopItem
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.isActive) {
            LIST_ITEM_IS_ACTIVE
        } else {
            LIST_ITEM_NOT_ACTIVE
        }
    }

    companion object {
        const val LIST_ITEM_IS_ACTIVE = 100
        const val LIST_ITEM_NOT_ACTIVE = 101
        const val MAX_PULL_SIZE = 15
    }
}