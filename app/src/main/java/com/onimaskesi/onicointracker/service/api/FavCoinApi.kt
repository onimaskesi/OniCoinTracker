package com.onimaskesi.onicointracker.service.api

import com.onimaskesi.onicointracker.model.favcoin.FavCoinsList
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface FavCoinApi {

    @Headers("X-CMC_PRO_API_KEY: b8710ee3-1dd5-45da-9765-1832b9cc1fe3")
    @GET("v1/cryptocurrency/quotes/latest")
    fun getFavCoins(
        @Query("symbol") symbol: String?
    ) : Single<FavCoinsList>

}