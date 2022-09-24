package com.vladimir.shoppinglist.data.mapper

import com.vladimir.shoppinglist.data.database.ShopItemDbModel
import com.vladimir.shoppinglist.domain.ShopItem
import javax.inject.Inject

class ShopListMapper @Inject constructor() {

    fun mapEntityToDbModel(shopItem: ShopItem) = ShopItemDbModel(
        id = shopItem.id,
        name = shopItem.name,
        count = shopItem.count,
        isActive = shopItem.isActive
    )

    fun mapDbModelToEntity(shopItemDbModel: ShopItemDbModel) = ShopItem(
        id = shopItemDbModel.id,
        name = shopItemDbModel.name,
        count = shopItemDbModel.count,
        isActive = shopItemDbModel.isActive
    )

    fun mapListDbModelToListEntity(list: List<ShopItemDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}