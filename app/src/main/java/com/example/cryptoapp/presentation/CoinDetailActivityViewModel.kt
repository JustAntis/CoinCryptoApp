package com.example.cryptoapp.presentation

import androidx.lifecycle.ViewModel
import com.example.cryptoapp.domain.usecases.GetCoinInfoUseCase
import javax.inject.Inject

class CoinDetailActivityViewModel @Inject constructor(
    private val getCoinInfoUseCase: GetCoinInfoUseCase
): ViewModel() {
    fun getDetailInfo(fSym: String) =  getCoinInfoUseCase(fSym)
}