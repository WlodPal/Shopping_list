package com.vladimir.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vladimir.shoppinglist.R
import com.vladimir.shoppinglist.domain.ShopItem

class ShopItemAdapter : RecyclerView.Adapter<ShopItemAdapter.ShopItemViewHolder>() {

    var shopList = listOf<ShopItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    class ShopItemViewHolder(val view : View): RecyclerView.ViewHolder(view){
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvCount = view.findViewById<TextView>(R.id.tv_count)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_shop_enabled,
            parent,
            false
        )
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = shopList[position]
        val status = if (shopItem.isActive){
            "Active"
        }else {
            "Not Active"
        }
        holder.view.setOnLongClickListener{

            true
        }

        if (shopItem.isActive){
            holder.tvName.text = "${shopItem.name} $status"
            holder.tvCount.text = shopItem.count.toString()
            holder.tvName.setTextColor(ContextCompat.getColor(holder.view.context, R.color.black))
        }
    }

    override fun onViewRecycled(holder: ShopItemViewHolder) {
        super.onViewRecycled(holder)
        holder.tvName.text = ""
        holder.tvCount.text = ""
        holder.tvName.setTextColor(ContextCompat.getColor(holder.view.context, R.color.white))
    }

    override fun getItemCount(): Int {
        return shopList.size
    }
}