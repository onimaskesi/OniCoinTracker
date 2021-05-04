package com.onimaskesi.onicointracker.view

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.onimaskesi.onicointracker.R
import com.onimaskesi.onicointracker.adapter.CoinClickListener
import com.onimaskesi.onicointracker.adapter.CoinRecyclerAdapter
import com.onimaskesi.onicointracker.adapter.FavBtnClickListener
import com.onimaskesi.onicointracker.model.Coin
import com.onimaskesi.onicointracker.viewmodel.FavViewModel
import kotlinx.android.synthetic.main.coin_recycler_raw.view.*
import kotlinx.android.synthetic.main.fav_fragment.*

class FavFragment : Fragment(), FavBtnClickListener, CoinClickListener {

    private lateinit var viewModel: FavViewModel
    private var recyclerCoinAdapter = CoinRecyclerAdapter(arrayListOf(),this, this)

    var coinList : ArrayList<Coin> = arrayListOf()

    var preferences : SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fav_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(FavViewModel::class.java)
        viewModel.getFavCoins()

        favListRecyclerView.adapter = recyclerCoinAdapter

        observeLiveData()

        favSwipeRefreshLayout.setOnRefreshListener {
            favListProgressBar.visibility = View.VISIBLE
            favListErrorMessage.visibility = View.GONE
            favListRecyclerView.visibility = View.GONE

            viewModel.getFavCoins()

            favSwipeRefreshLayout.isRefreshing = false
        }

        context?.let{
            preferences = it.getSharedPreferences("Oni", MODE_PRIVATE)
        }

        handleAd()

    }

    private fun observeLiveData(){

        favListErrorMessage.visibility = View.INVISIBLE
        favListProgressBar.visibility = View.VISIBLE

        viewModel.favCoinLoading.observe(viewLifecycleOwner, Observer{ isLoading ->
            isLoading?.let {
                if(isLoading){
                    favListProgressBar.visibility = View.VISIBLE
                } else {
                    favListProgressBar.visibility = View.GONE
                }
            }
        })

        viewModel.favCoinErrorMessage.observe(viewLifecycleOwner, Observer{ isError ->
            isError?.let {
                if(isError){
                    favListRecyclerView.visibility = View.GONE
                    favListErrorMessage.visibility = View.VISIBLE
                } else {
                    favListErrorMessage.visibility = View.GONE
                }
            }
        })

        viewModel.favCoinIsEmpty.observe(viewLifecycleOwner, Observer { isEmpty ->
            isEmpty?.let {
                if (isEmpty) {

                    favListProgressBar.visibility = View.GONE
                    favListRecyclerView.visibility = View.GONE

                    val yellowHeart = "💛"
                    val text = getString(R.string.fav_empty_text) + "\n\n" + "$yellowHeart"
                    favListErrorMessage.text = text
                    favListErrorMessage.visibility = View.VISIBLE

                }
            }
        })

        viewModel.favCoins.observe(viewLifecycleOwner, Observer { coins ->

            coins?.let{
                favListRecyclerView.visibility = View.VISIBLE

                coinList.clear()
                coinList.addAll(coins)

                updateRecyclerView()

            }

        })


    }

    private fun updateRecyclerView(){

        recyclerCoinAdapter.updateCoinList(coinList)

    }


    private fun handleAd(){
        //when go to the details and come back than don't show ad
        preferences?.let { prf ->
            val showAd = prf.getInt("showAd", 1)
            if(showAd == 1){
                showAd()
            }
            else{
                prf.edit().putInt("showAd", 1).apply()
            }
        }

    }

    private fun showAd(){
        val mainActivity = activity as MainActivity
        mainActivity.createInterstitialAd()
    }



    override fun coinClick(view: View) {

        preferences?.let{ prf ->
            prf.edit().putInt("showAd", 0).apply()
        }

        val action = FavFragmentDirections.actionFavFragmentToCoinDetailFragment(view.coinId.text.toString().toInt())
        Navigation.findNavController(view).navigate(action)
    }

    override fun favBtnClick(view: View) {

        val coinId = view.coinIdTV.text.toString().toInt()
        var clickedCoin : Coin? = null

        for (coin in coinList){
            if(coin.id.toInt() == coinId){
                clickedCoin = coin
                break
            }
        }

        clickedCoin?.let{
            if(clickedCoin.isFavorite != 0){

                clickedCoin.isFavorite = 0

                viewModel.deleteFavCoin(coinId)

                updateRecyclerView()
            }

        }

    }

}