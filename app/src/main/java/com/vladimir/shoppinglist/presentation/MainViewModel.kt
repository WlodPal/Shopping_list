package com.vladimir.shoppinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vladimir.shoppinglist.data.ShopListRepositoryImpl
import com.vladimir.shoppinglist.domain.DeleteShopItemUseCase
import com.vladimir.shoppinglist.domain.EditShopItemUseCase
import com.vladimir.shoppinglist.domain.GetShopListUseCase
import com.vladimir.shoppinglist.domain.ShopItem


// если нужен контекст то наследуемся от AndroidViewModel
// если нет тогда просто ViewModel
class MainViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl

    // в качестве конструктора везде нужно передать репозиторий
    // мы передадим эту реализация не правильным образом
    // создадим её прямо здесь во ViewModel
    // чтобы зделать правильно нужна иньекция зависимостей
    // почему это не правильно, так как презентейен слой
    // не должен знать о data слое также в date не должно быть презентейшен слоя
    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    // взаимодествие Activity и ViewModel должно проходить через LiveData
    // у нас должен быть обьек LiveData на который можно подписатся через
    // Activity или  fragments в этом случае будет успешно обрабатыватся например
    // переворот экрана. Если мы перевернем экран то Activity отпишется от обьекта LiveData
    // екран будет уничтожен вызван метод onDestroy() после Activity пересоздастся и
    // снова подпишется на обьект LiveData получив  у него последнее значение


    //создаем обьект LiveData
    // val shopList = LiveData<List<ShopItem>>() - не возможно создать экземпляр
    // абстрактного класса но у класса LiveData есть наследник MutableLiveData
    // MutableLiveData это наследник в который мы можем сами вставлять обьекты
//    val shopList = MutableLiveData<List<ShopItem>>()

    val shopList = getShopListUseCase.getShopList()


//    fun getShopList() {
//        val list = getShopListUseCase.getShopList()
//
//        // вставляем получиное значение в нашу LiveData
//        // можно сделать 2мя способами
//        // 1й setValue в котлине просто value
//        // 2й postValue к который мы передаем обьект
//        // они отличаются только тем что setValue можно вызывать
//        // только из главного потока, если вызвать его не из главного потока то прилодение
//        // упадет. А метод postValue модно вызывать из любого потока
//
//        shopList.value = list
//    }

    fun deleteShopItem(shopItem: ShopItem) {
        deleteShopItemUseCase.deleteShopItem(shopItem)
//        getShopList()
    }

    fun isActiveChange(shopItem: ShopItem) {
        val newItem = shopItem.copy(isActive = !shopItem.isActive)
        editShopItemUseCase.editShopItem(newItem)
//        getShopList()
    }
}