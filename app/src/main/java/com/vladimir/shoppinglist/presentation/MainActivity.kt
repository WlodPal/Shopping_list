package com.vladimir.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.vladimir.shoppinglist.R

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: ShopItemAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.shopList.observe(this) {
            adapter.shopList = it
        }


    }


    private fun setUpRecyclerView() {
        val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_list)
        with(rvShopList) {
            adapter = ShopItemAdapter()
            rvShopList.adapter = adapter
            recycledViewPool.setMaxRecycledViews(
                R.layout.item_shop_enabled,
                ShopItemAdapter.MAX_PULL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                R.layout.item_shop_disabled,
                ShopItemAdapter.MAX_PULL_SIZE
            )
        }
    }

}

