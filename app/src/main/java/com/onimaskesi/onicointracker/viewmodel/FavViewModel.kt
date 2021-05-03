package com.onimaskesi.onicointracker.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Embedded
import com.google.gson.annotations.SerializedName
import com.onimaskesi.onicointracker.model.Coin
import com.onimaskesi.onicointracker.model.CoinList
import com.onimaskesi.onicointracker.model.FavCoinsList
import com.onimaskesi.onicointracker.model.USD
import com.onimaskesi.onicointracker.service.CoinDatabase
import com.onimaskesi.onicointracker.service.FavCoinApiService
import com.onimaskesi.onicointracker.util.TimeCheckSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class FavViewModel(application : Application) : BaseViewModel(application) {
    val favCoins = MutableLiveData<List<Coin>>()
    val favCoinErrorMessage = MutableLiveData<Boolean>()
    val favCoinLoading = MutableLiveData<Boolean>()

    private val favCoinApiService = FavCoinApiService()
    private val disposable = CompositeDisposable()

    fun getFavCoins(){

        favCoinLoading.value = true

        launch{

            val favCoinDao = CoinDatabase(getApplication()).favCoinDao()
            val favCoins = favCoinDao.getFavCoins()
            var favCoinsSymbols = ""

            for(favCoin in favCoins){
                favCoinsSymbols += favCoin.sym
                if(favCoins[favCoins.size-1] != favCoin){
                    favCoinsSymbols += ","
                }
            }

            disposable.add(
                favCoinApiService.getData(favCoinsSymbols)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<FavCoinsList>(){
                        override fun onSuccess(t: FavCoinsList) {

                            t.favCoins?.let {

                                val coinList = arrayListOf<Coin>()
                                coinList.addAll(it.values.toList())

                                for(coin in coinList){
                                    coin.isFavorite = 1
                                }
                                setCoins(coinList)

                            }
                            //Toast.makeText(getApplication(), "API", Toast.LENGTH_SHORT).show()

                        }

                        override fun onError(e: Throwable) {
                            setError(e)
                        }

                    })
            )

        }

    }

    fun setCoins(favCoinList: List<Coin>){

        storeInSQlite(favCoinList)

        favCoins.value = favCoinList
        favCoinErrorMessage.value = false
        favCoinLoading.value = false

    }

    fun storeInSQlite(favCoinList: List<Coin>){
        launch{
            val coinDao = CoinDatabase(getApplication()).coinDao()

            for(favCoin in favCoinList){
                if(coinDao.getCoin(favCoin.id.toInt()) == null){

                    coinDao.insertAll(favCoin)
                }
            }

        }
    }

    fun setError(e : Throwable){
        favCoinErrorMessage.value = true
        favCoinLoading.value = false
        Log.e("Api", "FavCoin Api Error: " + e.message.toString())
    }

    fun deleteFavCoin(coinId: Int) {
        launch {
            val favCoinDao = CoinDatabase(getApplication()).favCoinDao()

            favCoinDao.deleteFromId(coinId)

        }
    }


}