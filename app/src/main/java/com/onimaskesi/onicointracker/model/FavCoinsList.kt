package com.onimaskesi.onicointracker.model

import com.google.gson.annotations.SerializedName

data class FavCoinsList (

    @SerializedName("data")
    val favCoins : Map<String, Coin>?

)