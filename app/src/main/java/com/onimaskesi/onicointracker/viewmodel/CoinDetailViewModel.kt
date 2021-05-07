package com.onimaskesi.onicointracker.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.onimaskesi.onicointracker.model.coin.Coin
import com.onimaskesi.onicointracker.service.database.CoinDatabase
import kotlinx.coroutines.launch

class CoinDetailViewModel(application : Application) : BaseViewModel(application) {
    val coinLiveData = MutableLiveData<Coin>()

    fun getCoinFromRoom(coinId : Int){
        launch {
            val coinDao = CoinDatabase(getApplication()).coinDao()
            coinLiveData.value = coinDao.getCoin(coinId)
        }
    }

}