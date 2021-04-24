package com.onimaskesi.onicointracker.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.onimaskesi.onicointracker.R
import com.onimaskesi.onicointracker.databinding.FragmentCoinDetailBinding
import com.onimaskesi.onicointracker.viewmodel.CoinDetailViewModel

class CoinDetailFragment : Fragment() {

    private var coinId = 0

    private lateinit var viewModel : CoinDetailViewModel

    private lateinit var dataBinding : FragmentCoinDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_coin_detail, container, false)
        return dataBinding.root
        //return inflater.inflate(R.layout.fragment_coin_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let{
            coinId = CoinDetailFragmentArgs.fromBundle(it).coinId
        }

        viewModel = ViewModelProviders.of(this).get(CoinDetailViewModel::class.java)
        viewModel.getCoinFromRoom(coinId)

        observeLiveData()

    }

    fun observeLiveData(){
        viewModel.coinLiveData.observe(viewLifecycleOwner, Observer { coin ->

            coin?.let {
                //fillTheViewValues(it)
                dataBinding.chosenCoin = it
                //changePercentsColors()
            }

        })
    }

    fun changeColorPercent(percentTV : TextView){
        val value = percentTV.text
        if(value.get(0) == '-'){
            percentTV.setTextColor(Color.RED)
        }
        else {
            percentTV.setTextColor(Color.GREEN)
        }
    }

    fun changePercentsColors(){
        changeColorPercent(dataBinding.onehourpercentTV)
        changeColorPercent(dataBinding.onedaypercentTV)
        changeColorPercent(dataBinding.sevendaypercentTV)
        changeColorPercent(dataBinding.thirtydaypercentTV)
        changeColorPercent(dataBinding.sixtydaypercentTV)
        changeColorPercent(dataBinding.ninetydaypercentTV)
    }

    /*
    fun fillTheViewValues(coin : Coin){

        this.context?.let {
            coinDetailIconTV.downloadImage("https://cryptoicons.org/api/icon/${coin.sym!!.toLowerCase()}/200.png", createPlaceholder(it))
        }

        coinNameTV.text = coin.name
        coinSymbolTV.text = coin.sym
        coinUsdTV.text = coin.quote?.usd?.price + "$"
        coinVolumeTV.text = coin.quote?.usd?.volume

        onehourpercentTV.text = coin.quote?.usd?.oneHourPercent + "%"
        changeColorPercent(onehourpercentTV)

        onedaypercentTV.text = coin.quote?.usd?.oneDayPercent + "%"
        changeColorPercent(onedaypercentTV)

        sevendaypercentTV.text = coin.quote?.usd?.sevenDayPercent + "%"
        changeColorPercent(sevendaypercentTV)

        thirtydaypercentTV.text = coin.quote?.usd?.thirtyDayPercent + "%"
        changeColorPercent(thirtydaypercentTV)

        sixtydaypercentTV.text = coin.quote?.usd?.sixtyDayPercent + "%"
        changeColorPercent(sixtydaypercentTV)

        ninetydaypercentTV.text = coin.quote?.usd?.ninetyDayPercent + "%"
        changeColorPercent(ninetydaypercentTV)
    }

     */

}