package com.onimaskesi.onicointracker.viewmodel

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.onimaskesi.onicointracker.model.coin.Coin
import com.onimaskesi.onicointracker.service.database.CoinDatabase
import kotlinx.coroutines.launch

class CoinDetailViewModel @ViewModelInject constructor(
    application : Application,
    private val coinDatabase: CoinDatabase
) : BaseViewModel(application) {

    val coinLiveData = MutableLiveData<Coin>()

    fun getCoinFromRoom(coinId : Int){
        launch {
            val coinDao = coinDatabase.coinDao()
            coinLiveData.value = coinDao.getCoin(coinId)
        }
    }

}