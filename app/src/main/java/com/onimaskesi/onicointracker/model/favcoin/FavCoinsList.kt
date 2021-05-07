package com.onimaskesi.onicointracker.model.favcoin

import com.google.gson.annotations.SerializedName
import com.onimaskesi.onicointracker.model.coin.Coin

data class FavCoinsList (

    @SerializedName("data")
    val favCoins : Map<String, Coin>?

)