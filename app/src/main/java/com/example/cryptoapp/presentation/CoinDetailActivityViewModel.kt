package com.example.cryptoapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.work.WorkManager
import com.example.cryptoapp.data.repository.CoinRepositoryImpl
import com.example.cryptoapp.data.worker.CoinSyncScheduler
import com.example.cryptoapp.data.worker.CoinSyncWorker
import com.example.cryptoapp.domain.usecases.GetCoinInfoUseCase
import com.example.cryptoapp.domain.usecases.GetCoinListUseCase
import com.example.cryptoapp.domain.usecases.LoadDataUseCase

class CoinDetailActivityViewModel(application: Application): AndroidViewModel(application) {
    private val repository = CoinRepositoryImpl(application)
    private val getCoinInfo = GetCoinInfoUseCase(repository)
    fun getDetailInfo(fSym: String) =  getCoinInfo(fSym)
}