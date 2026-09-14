package com.example.cryptoapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.domain.usecases.GetCoinListUseCase
import com.example.cryptoapp.domain.usecases.LoadDataUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class CoinInfoListActivityViewModel @Inject constructor(
    private val getCoinListUseCase: GetCoinListUseCase,
    private val loadDataUseCase: LoadDataUseCase
) : ViewModel() {
    private val getCoinListInfo = getCoinListUseCase
    private val loadData = loadDataUseCase

    val coinListInfo = getCoinListInfo()


    init {
        viewModelScope.launch {
            loadData()
        }
    }
}
