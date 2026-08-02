package com.example.cryptoapp.data.worker

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner

class CryptoCurrencyApp: Application() {

    override fun onCreate() {
        super.onCreate()

        ProcessLifecycleOwner
            .get()
            .lifecycle
            .addObserver(ApplicationObserver(this))
    }
}