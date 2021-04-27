package com.onimaskesi.onicointracker.model

import com.google.gson.annotations.SerializedName

data class Data(

    @SerializedName("BTC")
    val btc : Coin,

    @SerializedName("ETH")
    val eth : Coin
)