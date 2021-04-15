package com.onimaskesi.onicointracker.util

class CreateIconUrlFromCoinSymbol {

    val base_url_start = "https://cryptoicons.org/api/icon/"
    val base_url_end = "/200.png"

    fun create(coinSymbol : String) : String{
        val url = base_url_start + coinSymbol.toLowerCase() + base_url_end
        return url
    }

}