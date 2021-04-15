package com.onimaskesi.onicointracker.service

import com.onimaskesi.onicointracker.model.Coin
import com.onimaskesi.onicointracker.model.CoinList
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CoinApiService {

    val BASE_URL = "https://pro-api.coinmarketcap.com/"
    val api =
            Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(CoinApi :: class.java)

    fun getData(start : Int, limit : Int, convert : String) : Single<CoinList> {
        return api.getCoins(start,limit,convert)
    }
}