package com.vladimir.shoppinglist.presentation

import android.app.Application
import com.vladimir.shoppinglist.di.DaggerApplicationComponent

class ShopListApp: Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}