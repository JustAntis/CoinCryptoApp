package com.example.cryptoapp.di

import android.app.Application
import android.content.Context
import com.example.cryptoapp.data.worker.CryptoCurrencyApp
import com.example.cryptoapp.presentation.CoinDetailFragment
import com.example.cryptoapp.presentation.CoinInfoListActivity
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class, WorkerModule::class])
interface ApplicationComponent {

    fun inject(coinInfoListActivity: CoinInfoListActivity)

    fun inject(fragment: CoinDetailFragment)

    fun inject(application: CryptoCurrencyApp)

    @Component.Factory
    interface Factory{
        fun create(
            @BindsInstance context: Context,
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}