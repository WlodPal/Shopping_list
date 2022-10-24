package com.vladimir.shoppinglist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladimir.shoppinglist.domain.DeleteShopItemUseCase
import com.vladimir.shoppinglist.domain.EditShopItemUseCase
import com.vladimir.shoppinglist.domain.GetShopListUseCase
import com.vladimir.shoppinglist.domain.ShopItem
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainViewModel @Inject constructor(
    private val getShopListUseCase: GetShopListUseCase,
    private val deleteShopItemUseCase: DeleteShopItemUseCase,
    private val editShopItemUseCase: EditShopItemUseCase
) : ViewModel() {

    val shopList = getShopListUseCase.getShopList()


    fun deleteShopItem(shopItem: ShopItem) {
        viewModelScope.launch {
            deleteShopItemUseCase.deleteShopItem(shopItem)
        }
    }

    fun isActiveChange(shopItem: ShopItem) {
        viewModelScope.launch {
            val newItem = shopItem.copy(isActive = !shopItem.isActive)
            editShopItemUseCase.editShopItem(newItem)
        }
    }








}