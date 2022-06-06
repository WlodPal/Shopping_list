package com.vladimir.shoppinglist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vladimir.shoppinglist.R
import com.vladimir.shoppinglist.domain.ShopItem

class ShopItemAdapter : RecyclerView.Adapter<ShopItemAdapter.ShopItemViewHolder>() {

    var shopList = listOf<ShopItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var count = 0




    class ShopItemViewHolder(val view : View): RecyclerView.ViewHolder(view){
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvCount = view.findViewById<TextView>(R.id.tv_count)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        Log.d("onCreateViewHolder", "$count")
        count++
        val layout = when (viewType){
            LIST_ITEM_IS_ACTIVE -> R.layout.item_shop_enabled
            LIST_ITEM_NOT_ACTIVE -> R.layout.item_shop_disabled
            else -> throw RuntimeException("Unknown viewType $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ShopItemViewHolder(view)

    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = shopList[position]

        holder.view.setOnLongClickListener{

            true
        }
        holder.tvName.text = shopItem.name
        holder.tvCount.text = shopItem.count.toString()

    }

    override fun onViewRecycled(holder: ShopItemViewHolder) {
        super.onViewRecycled(holder)
        holder.tvName.text = ""
        holder.tvCount.text = ""
    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    override fun getItemViewType(position: Int): Int {
        val item = shopList[position]
        return if (item.isActive){
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