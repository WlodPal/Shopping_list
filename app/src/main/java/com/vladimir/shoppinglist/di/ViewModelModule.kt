package com.vladimir.shoppinglist.di

import androidx.lifecycle.ViewModel
import com.vladimir.shoppinglist.presentation.MainViewModel
import com.vladimir.shoppinglist.presentation.ShopItemViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ShopItemViewModel::class)
    fun bindsShopItemViewModel(viewModel: ShopItemViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindsMainViewModel(viewModel: MainViewModel): ViewModel
}
