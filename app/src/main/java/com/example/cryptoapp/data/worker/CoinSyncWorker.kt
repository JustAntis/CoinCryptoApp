package com.example.cryptoapp.data.worker

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkerParameters
import com.example.cryptoapp.data.database.CoinInfoDao
import com.example.cryptoapp.data.mappers.CoinMapper
import com.example.cryptoapp.data.network.ApiService
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

class CoinSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val apiService: ApiService,
    private val mapper: CoinMapper,
    private val coinInfoDao: CoinInfoDao
) :
    CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        Log.d("WORK_MANAGER_TAG", "doWork")
        return try {
            val topCoins = apiService.getTopCoinsInfo(limit = 50)
            val fSyms = mapper.mapNamesListToString(topCoins)
            val jsonContainer = apiService.getFullPriceList(fSyms = fSyms)
            val coinInfoDtoList = mapper.mapJsonContainerToListCoinInfo(jsonContainer)
            val dbModelList = coinInfoDtoList.map { mapper.mapDtoToDbModel(it) }
            coinInfoDao.insertCoinInfoList(dbModelList)
            saveLastSyncTime(applicationContext)
            Result.success()
        } catch (e: Exception) {
            // No retry: the API quota is too small to spend on repeated attempts
            Result.failure()
        }
    }

    companion object {
        const val WORKER_NAME = "coin_sync_worker"
        const val PERIODIC_WORKER_NAME = "coin_sync_periodic_worker"

        private const val PREFS_NAME = "coin_sync_prefs"
        private const val KEY_LAST_SYNC_TIME = "last_sync_time"
        private const val SYNC_INTERVAL_HOURS = 24L
        private val MIN_REFRESH_INTERVAL_MILLIS = TimeUnit.HOURS.toMillis(12)

        fun makeRequest(): OneTimeWorkRequest {
            return OneTimeWorkRequest.Builder(
                CoinSyncWorker::class.java,
            )
                .setConstraints(makeConstraints())
                .build()
        }

        fun makePeriodicRequest(): PeriodicWorkRequest {
            return PeriodicWorkRequest.Builder(
                CoinSyncWorker::class.java,
                SYNC_INTERVAL_HOURS,
                TimeUnit.HOURS
            )
                .setInitialDelay(SYNC_INTERVAL_HOURS, TimeUnit.HOURS)
                .setConstraints(makeConstraints())
                .build()
        }

        fun isRefreshNeeded(context: Context): Boolean {
            val lastSyncTime = getPrefs(context).getLong(KEY_LAST_SYNC_TIME, 0L)
            return System.currentTimeMillis() - lastSyncTime >= MIN_REFRESH_INTERVAL_MILLIS
        }

        private fun saveLastSyncTime(context: Context) {
            getPrefs(context).edit()
                .putLong(KEY_LAST_SYNC_TIME, System.currentTimeMillis())
                .apply()
        }

        private fun getPrefs(context: Context) =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        private fun makeConstraints(): Constraints {
            return Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        }
    }
    @AssistedFactory
    interface Factory: ChildWorkerFactory{
        override fun create(
            context: Context,
            workerParameters: WorkerParameters
        ): CoinSyncWorker
    }
}