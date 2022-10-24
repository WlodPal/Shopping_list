package com.vladimir.shoppinglist.di

import android.app.Application
import com.vladimir.shoppinglist.data.database.AppDatabase
import com.vladimir.shoppinglist.data.database.ShopListDao
import com.vladimir.shoppinglist.data.repository.ShopListRepositoryImpl
import com.vladimir.shoppinglist.domain.ShopListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @Binds
    @ApplicationScope
    fun bindShopListRepository(impl: ShopListRepositoryImpl): ShopListRepository

    companion object {

        @Provides
        @ApplicationScope
        fun provideShopListDao(
            application: Application
        ): ShopListDao {
            return AppDatabase.getInstance(application).shopListDao()
        }

    }
}
