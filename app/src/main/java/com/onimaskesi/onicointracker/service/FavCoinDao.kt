package com.onimaskesi.onicointracker.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.onimaskesi.onicointracker.model.FavCoin

@Dao
interface FavCoinDao {

    @Insert
    suspend fun insertAll(vararg favCoin : FavCoin) : List<Long>

    @Query("SELECT * FROM FavCoin")
    suspend fun getFavCoins(): List<FavCoin>

    @Query("SELECT * FROM FavCoin WHERE id = :favCoinId ")
    suspend fun getFavCoin(favCoinId : Int): FavCoin

    @Query("DELETE FROM FavCoin")
    suspend fun deleteAll()

}