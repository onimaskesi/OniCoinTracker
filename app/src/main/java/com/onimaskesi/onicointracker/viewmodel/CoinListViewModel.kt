package com.onimaskesi.onicointracker.viewmodel

import android.app.Application
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.onimaskesi.onicointracker.model.coin.Coin
import com.onimaskesi.onicointracker.model.coin.CoinList
import com.onimaskesi.onicointracker.model.favcoin.FavCoin
import com.onimaskesi.onicointracker.service.api.CoinApiService
import com.onimaskesi.onicointracker.service.database.CoinDatabase
import com.onimaskesi.onicointracker.util.TimeCheckSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class CoinListViewModel @ViewModelInject constructor(
        application : Application,
        private val coinApiService : CoinApiService,
        private val coinDatabase : CoinDatabase
) : BaseViewModel(application) {

    val coins = MutableLiveData<List<Coin>>()
    val coinErrorMessage = MutableLiveData<Boolean>()
    val coinLoading = MutableLiveData<Boolean>()

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

            val coinDao = coinDatabase.coinDao()
            val favCoinDao = coinDatabase.favCoinDao()

            val coinsThatIsNotInDB = arrayListOf<Coin>()

            if(!isLoadMore){
                coinDao.deleteAll()
            }


            for(coin in coinList){

                //if coins is fav than isFavorite variable is 1 else 0
                if(favCoinDao.getFavCoin(coin.id.toInt()) == null){

                    coin.isFavorite = 0
                    coinsThatIsNotInDB.add(coin)
                } else {
                    coin.isFavorite = 1
                    if(coinDao.getCoin(coin.id.toInt()) == null){
                        coinsThatIsNotInDB.add(coin)
                    }
                }
            }

            coinDao.insertAll(*coinsThatIsNotInDB.toTypedArray())

            getDataFromSqliteWithRoom()

        }

        timeCheckSheredPreferences.saveTime(System.nanoTime())

    }


    fun getDataFromSqliteWithRoom(){

        coinLoading.value = true

        launch {
            val coinDao = coinDatabase.coinDao()
            val favCoinDao = coinDatabase.favCoinDao()
            val coinList = coinDao.getCoins()

            for(coin in coinList){
                if(favCoinDao.getFavCoin(coin.id.toInt()) == null){

                    coin.isFavorite = 0

                } else {
                    coin.isFavorite = 1
                }
            }

            setCoins(coinList)

        }
    }

    fun setFavCoin(favCoin: FavCoin) {
        launch {
            val favCoinDao = coinDatabase.favCoinDao()

            if(favCoinDao.getFavCoin(favCoin.id) == null){

                favCoinDao.insertAll(favCoin)

                Log.d("FavCoin", "added new Fav that's symbol is ${favCoinDao.getFavCoin(favCoin.id).sym}")
            }

        }
    }


}