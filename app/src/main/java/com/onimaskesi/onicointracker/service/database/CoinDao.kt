package com.onimaskesi.onicointracker.service.database

import androidx.room.*
import com.onimaskesi.onicointracker.model.coin.Coin

@Dao
interface CoinDao {

    @Insert
    suspend fun insertAll(vararg coin : Coin) : List<Long>

    @Update
    suspend fun updateCoin(vararg coin : Coin)

    @Query("SELECT * FROM Coin")
    suspend fun getCoins(): List<Coin>

    @Query("SELECT * FROM Coin WHERE id = :coinId ")
    suspend fun getCoin(coinId : Int): Coin

    @Query("DELETE FROM Coin")
    suspend fun deleteAll()

}