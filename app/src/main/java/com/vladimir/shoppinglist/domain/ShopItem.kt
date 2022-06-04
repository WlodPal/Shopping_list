package com.vladimir.shoppinglist.domain

data class ShopItem(
    // обезательные поля находятся первыми а не обезательные как ID в конце
    val name: String,
    val count: Int,
    val isActive: Boolean,
    var id: Int = UNDEFINED_ID

//делаем константу
) {
    companion object {
        const val UNDEFINED_ID = -1
    }
}


