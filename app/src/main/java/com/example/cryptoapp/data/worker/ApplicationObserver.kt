package com.example.cryptoapp.data.worker

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.work.WorkManager

class ApplicationObserver(application: Application): DefaultLifecycleObserver {
    private val workManager = WorkManager.getInstance(application)
    private val coinSyncManager = CoinSyncScheduler(workManager)
    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        coinSyncManager.startSync()
    }
}