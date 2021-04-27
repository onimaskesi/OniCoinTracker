package com.onimaskesi.onicointracker.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.onimaskesi.onicointracker.model.Coin
import com.onimaskesi.onicointracker.model.CoinList
import com.onimaskesi.onicointracker.model.FavCoin
import com.onimaskesi.onicointracker.service.CoinApiService
import com.onimaskesi.onicointracker.service.CoinDatabase
import com.onimaskesi.onicointracker.util.TimeCheckSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class CoinListViewModel(application : Application) : BaseViewModel(application) {

    val coins = MutableLiveData<List<Coin>>()
    val coinErrorMessage = MutableLiveData<Boolean>()
    val coinLoading = MutableLiveData<Boolean>()

    private val coinApiService = CoinApiService()
    private val disposable = CompositeDisposable()

    private val start = 1
    private val limit = 10
    private val convert = "USD"
    
    private val timeCheckSheredPreferences = TimeCheckSharedPreferences(getApplication())

    private val updateTime = 10 * 60 * 1000 * 1000 * 1000L // 10 minutes

    fun refreshData(){

        val savedTime = timeCheckSheredPreferences.getTime()

        if(savedTime != null && savedTime != 0L && System.nanoTime() -  savedTime < updateTime){
            getDataFromSqliteWithRoom()
            //Toast.makeText(getApplication(), "ROOM", Toast.LENGTH_SHORT).show()
        } else {
            getDataFromApiWithRetrofit(start, limit, convert)
        }

    }

    fun refreshFromInternet(start : Int, limit : Int){
        getDataFromApiWithRetrofit(start, limit, convert)
    }

    fun getDataFromApiWithRetrofit(start : Int, limit : Int, convert : String){
        coinLoading.value = true

        disposable.add(
                coinApiService.getData(start,limit,convert)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableSingleObserver<CoinList>(){
                            override fun onSuccess(t: CoinList) {

                                t.coins?.let {

                                    if(start == 1){
                                        storeInSQlite(it, false)
                                    } else {
                                        storeInSQlite(it, true)
                                    }


                                }
                                //Toast.makeText(getApplication(), "API", Toast.LENGTH_SHORT).show()

                            }

                            override fun onError(e: Throwable) {
                                setError(e)
                            }

                        })
        )

    }

    fun setCoins(coinList : List<Coin>?){

        coins.value = coinList!!
        coinErrorMessage.value = false
        coinLoading.value = false

    }

    fun setError(e : Throwable){
        coinErrorMessage.value = true
        coinLoading.value = false
        Log.e("Api", e.message.toString())
    }

    fun storeInSQlite(coinList : List<Coin>, isLoadMore : Boolean){

        launch {

            val coinDatabase = CoinDatabase(getApplication()) as CoinDatabase
            val coinDao = coinDatabase.coinDao()
            val favCoinDao = coinDatabase.favCoinDao()

            if(!isLoadMore){
                coinDao.deleteAll()
            }

            coinList?.let{
                for(coin in coinList){
                    if(favCoinDao.getFavCoin(coin.id.toInt()) == null){

                        coin.isFavorite = 0

                    } else {
                        coin.isFavorite = 1
                    }
                }

            }

            coinDao.insertAll(*coinList.toTypedArray())

            setCoins(coinList)

        }

        timeCheckSheredPreferences.saveTime(System.nanoTime())

    }

    fun getDataFromSqliteWithRoom(){

        coinLoading.value = true

        launch {
            val coinDatabase = CoinDatabase(getApplication()) as CoinDatabase
            val coinDao = coinDatabase.coinDao()
            setCoins(coinDao.getCoins())

        }
    }

    fun setFavCoin(favCoin: FavCoin) {
        launch {
            val coinDatabase = CoinDatabase(getApplication()) as CoinDatabase
            val favCoinDao = coinDatabase.favCoinDao()
            val coinDao = coinDatabase.coinDao()

            if(favCoinDao.getFavCoin(favCoin.id) == null){

                favCoinDao.insertAll(favCoin)

                val coin = coinDao.getCoin(favCoin.id)
                coin.isFavorite = 1
                coinDao.updateCoin(coin)
                //getDataFromSqliteWithRoom()

                Log.d("FavCoin", "added new Fav that's symbol is ${favCoinDao.getFavCoin(favCoin.id).sym}")
            }

        }
    }


}