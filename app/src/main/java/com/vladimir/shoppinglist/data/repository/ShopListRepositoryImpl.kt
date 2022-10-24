package com.vladimir.shoppinglist.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.vladimir.shoppinglist.data.database.ShopListDao
import com.vladimir.shoppinglist.data.mapper.ShopListMapper
import com.vladimir.shoppinglist.domain.ShopItem
import com.vladimir.shoppinglist.domain.ShopListRepository
import javax.inject.Inject


class ShopListRepositoryImpl @Inject constructor(
    private val shopListDao: ShopListDao,
    private val mapper: ShopListMapper
) : ShopListRepository {


    override suspend fun addShopItem(shopItem: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    override suspend fun deleteShopItem(shopItem: ShopItem) {
        shopListDao.deleteShopItem(shopItem.id)
    }

    override suspend fun editShopItem(shopItem: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    override suspend fun getShopItem(shopItemId: Int): ShopItem {
        val dbModel = shopListDao.getShopItem(shopItemId)
        return mapper.mapDbModelToEntity(dbModel)
    }

    override fun getShopList(): LiveData<List<ShopItem>> = Transformations.map(
        shopListDao.getShopList()
    ) {
        mapper.mapListDbModelToListEntity(it)
    }


    //можно самому создать MediatorLiveData или использовать Transformations.map
//    override fun getShopList(): LiveData<List<ShopItem>> = MediatorLiveData<List<ShopItem>>().apply {
//        addSource(shopListDao.getShopList()){
//            value = mapper.mapListDbModelToListEntity(it)
//        }
//    }

}