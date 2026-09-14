package com.example.cryptoapp.di

import androidx.lifecycle.ViewModel
import com.example.cryptoapp.presentation.CoinDetailActivityViewModel
import com.example.cryptoapp.presentation.CoinInfoListActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(CoinDetailActivityViewModel::class)
    @Binds
    fun bindViewModelDetailActivity(impl: CoinDetailActivityViewModel): ViewModel

    @IntoMap
    @ViewModelKey(CoinInfoListActivityViewModel::class)
    @Binds
    fun bindViewModelListActivity(impl: CoinInfoListActivityViewModel): ViewModel


}