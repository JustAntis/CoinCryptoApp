package com.example.cryptoapp.data.worker

import android.app.Application
import androidx.work.Configuration
import com.example.cryptoapp.di.DaggerApplicationComponent
import javax.inject.Inject

class CryptoCurrencyApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: CoinSyncWorkerFactory
    val component by lazy {
        DaggerApplicationComponent.factory()
            .create(this, this)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        component.inject(this)
        super.onCreate()
    }
}