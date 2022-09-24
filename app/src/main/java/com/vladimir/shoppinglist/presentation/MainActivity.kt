package com.vladimir.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.vladimir.shoppinglist.R
import com.vladimir.shoppinglist.databinding.ActivityMainBinding
import com.vladimir.shoppinglist.presentation.adapters.ShopItemAdapter
import javax.inject.Inject

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }
    private lateinit var shopItemAdapter: ShopItemAdapter

    private lateinit var binding: ActivityMainBinding

    private val component by lazy {
        (application as ShopListApp).component
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpRecyclerView()
        binding.fabAddShoeItem.setOnClickListener {
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
        return binding.shopItemContainer == null
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
        with(binding.rvShopList) {
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
        setUpSwipeDelete(binding.rvShopList)
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





