package com.example.cryptoapp.data.network.model

import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.inject.Inject

data class CoinInfoJsonContainerDto (
    @SerializedName("RAW")
    @Expose
    val coinPriceInfoJsonObject: JsonObject
)
