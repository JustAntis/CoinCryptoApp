package com.example.cryptoapp.data.worker

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager

class CoinSyncScheduler(private val workManager: WorkManager) {
    fun startSync(){
        workManager.enqueueUniquePeriodicWork(
            CoinSyncWorker.WORKER_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            CoinSyncWorker.makeRequest()
        )
    }
}