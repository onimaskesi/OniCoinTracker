package com.onimaskesi.onicointracker.model.coin

import com.google.gson.annotations.SerializedName

data class CoinList (
    @SerializedName("data")
    val coins: List<Coin>?
)