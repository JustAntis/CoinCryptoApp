package com.example.cryptoapp.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.cryptoapp.data.database.AppDatabase
import com.example.cryptoapp.data.mappers.CoinMapper
import com.example.cryptoapp.data.network.ApiFactory
import com.example.cryptoapp.domain.entities.CoinInfo
import com.example.cryptoapp.domain.repository.CoinRepository
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class CoinRepositoryImpl(private val context: Context) : CoinRepository {

    private val coinInfoDao = AppDatabase.getInstance(context).coinPriceInfoDao()
    private val mapper = CoinMapper()
    private val apiService = ApiFactory.apiService

    override fun getCoinInfoList(): LiveData<List<CoinInfo>> {
        val coinList = coinInfoDao.getCoinInfoList()
        return coinList.map { models ->
            models.map {
                mapper.mapDbModelToEntity(it)
            }
        }
    }

    override fun getCoinInfo(fromSymbol: String): LiveData<CoinInfo> {
        val coinInfo = coinInfoDao.getCoinInfo(fromSymbol)
        return coinInfo.map {
            mapper.mapDbModelToEntity(it)
        }
    }

    override suspend fun refreshData() {
        val topCoins = apiService.getTopCoinsInfo(limit = 50)
        val fSymbols = mapper.mapNamesListToString(topCoins)
        val jsonContainer = apiService.getFullPriceList(fSyms = fSymbols)
        val coinInfoDtoList = mapper.mapJsonContainerToListCoinInfo(jsonContainer)
        val dbModelList = coinInfoDtoList.map {
            mapper.mapDtoToDbModel(it)
        }
        coinInfoDao.insertCoinInfoList(dbModelList)
    }

    override suspend fun loadData() {
        while (true) {
            try {
                refreshData()
            } catch (e: Exception) {

            }
            delay(10.seconds)
        }
    }
}