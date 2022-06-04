package com.vladimir.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vladimir.shoppinglist.domain.ShopItem
import com.vladimir.shoppinglist.domain.ShopListRepository

// делаем object который является single tone тоесть где бы мы не обратисись к
// обьекту это будет один и тот же обьект это нужно чтобы не получилось так что мы
// работаем на одном экране с одним репозиторием а на другом с другим

object ShopListRepositoryImpl : ShopListRepository {

    private val shopListLD = MutableLiveData<List<ShopItem>>()

    private val shopList = mutableListOf<ShopItem>()

    private var autoIncrementId = 0



    init {
        for (i in 0 until 10) {
            val item = ShopItem("Name $i",i,true)
            addShopItem(item)
        }
    }


    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementId++
        }
        shopList.add(shopItem)
        updateList()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateList()
    }

    override fun editShopItem(shopItem: ShopItem) {
        // сначало находим старый элемент по ID удаляем его и
        // на его место вставляем новый
        val oldElement = getShopItem(shopItem.id)
        shopList.remove(oldElement)
        addShopItem(shopItem)
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopList.find {
            it.id == shopItemId
        } ?: throw RuntimeException("Element with id $shopItemId not found")

        // метод find он принимает в качастве параментра придикат футкцию
        // которая возвращает true или false
        // метод find возвращает нулабельный обьект так реализовано так
        // как обьект может быть не найден и тогда вернет null
        // если в нашей логики можеть быть null то мы меняем возвращаемый
        // тип на нулабельный но если не можеть быть null то бросаем исключение
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopListLD
//        return shopList.toList()
        // возврящать саму колекцию не хорошо потому что мы с других мест програмы
        // добавлять новые элементы в даную колекцию или удалять лучше
        // возвращать копию этой коллекции. Точно такую коллекцию с тем же набором
        // элементов но сам обьект будет другой. Если мы будем что-то делать с
        // этой колекциией то на иходную коллекцию это не повлияет
        // чтобы создать копию листа мы вызиваем метод toList()
        // если это изменяемая коллекция то метод toMutableList()
    }

    private fun updateList(){
        shopListLD.value = shopList.toList()
    }
}