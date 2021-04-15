package com.onimaskesi.onicointracker.service

import com.onimaskesi.onicointracker.model.Coin
import com.onimaskesi.onicointracker.model.CoinList
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CoinApi {

    @Headers("X-CMC_PRO_API_KEY: b8710ee3-1dd5-45da-9765-1832b9cc1fe3")
    @GET("v1/cryptocurrency/listings/latest")
    fun getCoins(
            @Query("start") start: Int?,
            @Query("limit") limit : Int?,
            @Query("convert") convert : String?
    ) : Single<CoinList>
}