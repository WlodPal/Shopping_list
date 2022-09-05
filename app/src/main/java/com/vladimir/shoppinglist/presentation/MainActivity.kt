package com.vladimir.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vladimir.shoppinglist.R
import com.vladimir.shoppinglist.domain.ShopItem

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var shopItemAdapter: ShopItemAdapter

    private var shopItemContainer: FragmentContainerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        shopItemContainer = findViewById(R.id.shop_item_container)
        setUpRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val buttonAddItem = findViewById<FloatingActionButton>(R.id.fab_add_shoe_item)
        buttonAddItem.setOnClickListener {
            if (isOnePaneMode()) {
                val intent = ShopItemActivity.newIntentAddItem(this)
                startActivity(intent)
            } else {
                lunchFragment(ShopItemFragment.newInstanceAddItem())
            }
        }
        viewModel.shopList.observe(this) {
            shopItemAdapter.submitList(it)
        }
    }


    override fun onEditingFinished() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }

    /*
    если находится в книжной ориентации то равно null
    если нет то в альбомной
     */
    private fun isOnePaneMode(): Boolean {
        return shopItemContainer == null
    }

    /*
    .add(R.id.shop_item_container, fragment) -> добавлет fragment в контейнер а с предвидущим
    ничего не делает
    .replace(R.id.shop_item_container, fragment) -> он берет старый фрагмент и удалит его
    и добавит новый

    .addToBackStack(null) -> нужно чтобы когда делаешь какието действия можно было
    перейти назад нажав кнопку назад а не перемещатся по активити

    supportFragmentManager.popBackStack() -> чтобы не открывались все предведущие экраны
    точнее убирает из backStack старый фрагмент а если его небыло то ничего делать не будет
     */
    private fun lunchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .addToBackStack(null)
            .commit()
    }


    private fun setUpRecyclerView() {
        val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_list)
        with(rvShopList) {
            shopItemAdapter = ShopItemAdapter()
            adapter = shopItemAdapter
            recycledViewPool.setMaxRecycledViews(
                R.layout.item_shop_enabled,
                ShopItemAdapter.MAX_PULL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                R.layout.item_shop_disabled,
                ShopItemAdapter.MAX_PULL_SIZE
            )
        }
        setUpLongClickListener()
        setUpShortClickListener()
        setUpSwipeDelete(rvShopList)
    }


    private fun setUpSwipeDelete(rvShopList: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = shopItemAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

    private fun setUpShortClickListener() {
        shopItemAdapter.onShopItemShortClickListener = {
            if (isOnePaneMode()) {
                val intent = ShopItemActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            }else {
                lunchFragment(ShopItemFragment.newInstanceEditItem(it.id))
            }
        }
    }

    private fun setUpLongClickListener() {
        shopItemAdapter.onShopItemLongClickListener = {
            viewModel.isActiveChange(it)
        }
    }
}





