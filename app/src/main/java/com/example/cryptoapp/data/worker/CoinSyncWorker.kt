package com.example.cryptoapp.data.worker

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.cryptoapp.data.repository.CoinRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CoinSyncWorker(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {
    private val coinRepository = CoinRepositoryImpl(context)

    override suspend fun doWork(): Result {
        Log.d("WORK_MANAGER_TAG", "doWork")
        try {
            coinRepository.refreshData()
            return Result.success()
        } catch (e: Exception){
            return Result.retry()
        }
    }

    companion object {
        const val WORKER_NAME = "coin_sync_worker"
        private const val SYNC_DATA_TIME_IN_HOURS = 1L
        private const val FLEX_INTERVAL = 15L

        fun makeRequest(): PeriodicWorkRequest {
            return PeriodicWorkRequest.Builder(
                CoinSyncWorker::class.java,
                SYNC_DATA_TIME_IN_HOURS,
                java.util.concurrent.TimeUnit.HOURS,
                FLEX_INTERVAL,
                java.util.concurrent.TimeUnit.MINUTES
            )
                .setConstraints(makeConstraints())
                .build()
        }
        private fun makeConstraints(): Constraints{
            return Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        }
    }
}