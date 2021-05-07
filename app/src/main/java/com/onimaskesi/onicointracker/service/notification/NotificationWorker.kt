package com.onimaskesi.onicointracker.service.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.MutableLiveData
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.onimaskesi.onicointracker.R
import com.onimaskesi.onicointracker.model.coin.Coin
import com.onimaskesi.onicointracker.model.favcoin.FavCoinsList
import com.onimaskesi.onicointracker.service.api.FavCoinApiService
import com.onimaskesi.onicointracker.service.database.CoinDatabase
import com.onimaskesi.onicointracker.util.RoundString
import com.onimaskesi.onicointracker.view.MainActivity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class NotificationWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams), CoroutineScope{

    val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun doWork(): Result{
        getFavCoinsAndAddNotificationToMostChangedOne()
        return Result.success()
    }

    fun getMostChangedCoin(coinList : List<Coin>): Coin {
        var mostChanged : Coin = coinList[0]
        for (i in coinList.indices){
            if(i != 0){
                if(getOneDayPercentChangeToCoin(coinList[i]) > getOneDayPercentChangeToCoin(mostChanged)){
                    mostChanged = coinList[i]
                }
            }
        }
        return mostChanged
    }

    private fun getOneDayPercentChangeToCoin(coin : Coin): Double {
        var oneDayPercent = coin.quote!!.usd.oneDayPercent
        if(oneDayPercent!![0] == '-'){
            oneDayPercent = oneDayPercent.subSequence(1,oneDayPercent.length-1).toString()
        }
        return oneDayPercent.toDouble()
    }

    fun addNotification(coin : Coin){

        val channelId = "channel_1"

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)

        val contentText = coin.name + " was " +
                RoundString().roundStringFun(coin.quote!!.usd.oneDayPercent.toString(), 2) + "%" +
                " change a day!!"

        val notification = NotificationCompat
            .Builder(applicationContext, channelId)
            .setContentTitle("The Coins Caught Fire! \uD83D\uDD25\uD83D\uDCB8\uD83D\uDD25")
            .setContentText(contentText)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_baseline_monetization_on_24)
            .build()

        NotificationManagerCompat.from(applicationContext).notify(1, notification)

    }

    private val favCoinApiService = FavCoinApiService()

    private fun getFavCoinsAndAddNotificationToMostChangedOne(){
        launch{
            val favCoinDao = CoinDatabase(applicationContext).favCoinDao()
            val favCoins = favCoinDao.getFavCoins()

            if(favCoinDao.getFavCoins().isEmpty()){
                return@launch
            }

            var favCoinsSymbols = ""

            for(favCoin in favCoins){
                favCoinsSymbols += favCoin.sym
                if(favCoins[favCoins.size-1] != favCoin){
                    favCoinsSymbols += ","
                }
            }

            CompositeDisposable().add(
                favCoinApiService.getData(favCoinsSymbols)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<FavCoinsList>(){
                        override fun onSuccess(t: FavCoinsList) {

                            t.favCoins?.let {

                                val coinList = arrayListOf<Coin>()
                                coinList.addAll(it.values.toList())

                                val mostChangedCoin = getMostChangedCoin(coinList)
                                addNotification(mostChangedCoin)

                            }
                            //Toast.makeText(getApplication(), "API", Toast.LENGTH_SHORT).show()

                        }

                        override fun onError(e: Throwable) {

                        }

                    })
            )
        }
    }

}