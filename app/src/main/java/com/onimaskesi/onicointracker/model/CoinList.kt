package com.onimaskesi.onicointracker.model

import com.google.gson.annotations.SerializedName

data class CoinList (
    @SerializedName("data")
    val coins: List<Coin>?
)