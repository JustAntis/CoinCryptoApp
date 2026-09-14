package com.example.cryptoapp.data.worker

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkerParameters
import com.example.cryptoapp.data.database.CoinInfoDao
import com.example.cryptoapp.data.mappers.CoinMapper
import com.example.cryptoapp.data.network.ApiService
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

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
        while (true) {
            try {
                val topCoins = apiService.getTopCoinsInfo(limit = 50)
                val fSyms = mapper.mapNamesListToString(topCoins)
                val jsonContainer = apiService.getFullPriceList(fSyms = fSyms)
                val coinInfoDtoList = mapper.mapJsonContainerToListCoinInfo(jsonContainer)
                val dbModelList = coinInfoDtoList.map { mapper.mapDtoToDbModel(it) }
                coinInfoDao.insertCoinInfoList(dbModelList)
            } catch (e: Exception) {
            }
            delay(10.seconds)
        }
    }

    companion object {
        const val WORKER_NAME = "coin_sync_worker"


        fun makeRequest(): OneTimeWorkRequest {
            return OneTimeWorkRequest.Builder(
                CoinSyncWorker::class.java,
            )
                .setConstraints(makeConstraints())
                .build()
        }

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