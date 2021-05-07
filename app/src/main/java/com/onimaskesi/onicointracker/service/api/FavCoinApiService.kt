package com.onimaskesi.onicointracker.service.api

import com.onimaskesi.onicointracker.model.favcoin.FavCoinsList
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class FavCoinApiService {
    val BASE_URL = "https://pro-api.coinmarketcap.com/"
    val api =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(FavCoinApi :: class.java)

    fun getData(symbol : String?) : Single<FavCoinsList> {
        return api.getFavCoins(symbol)
    }
}