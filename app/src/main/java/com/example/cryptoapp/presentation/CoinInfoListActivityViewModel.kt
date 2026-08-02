package com.example.cryptoapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.example.cryptoapp.data.repository.CoinRepositoryImpl
import com.example.cryptoapp.data.worker.CoinSyncScheduler
import com.example.cryptoapp.domain.usecases.GetCoinInfoUseCase
import com.example.cryptoapp.domain.usecases.GetCoinListUseCase
import com.example.cryptoapp.domain.usecases.LoadDataUseCase
import kotlinx.coroutines.launch

class CoinInfoListActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CoinRepositoryImpl(application)
    private val getCoinListInfo = GetCoinListUseCase(repository)
    private val loadData = LoadDataUseCase(repository)

    val coinListInfo = getCoinListInfo()


    init {
        viewModelScope.launch {
            loadData()
        }
    }
}
