package com.onimaskesi.onicointracker.view

import androidx.lifecycle.ViewModelProvider
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
import com.onimaskesi.onicointracker.model.FavCoin
import com.onimaskesi.onicointracker.viewmodel.CoinListViewModel
import com.onimaskesi.onicointracker.viewmodel.FavViewModel
import kotlinx.android.synthetic.main.coin_recycler_raw.view.*
import kotlinx.android.synthetic.main.fav_fragment.*
import kotlinx.android.synthetic.main.fragment_coin_list.*

class FavFragment : Fragment(), FavBtnClickListener, CoinClickListener {

    companion object {
        fun newInstance() = FavFragment()
    }

    private lateinit var viewModel: FavViewModel
    private var recyclerCoinAdapter = CoinRecyclerAdapter(arrayListOf(),this, this)

    var coinList : ArrayList<Coin> = arrayListOf()

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

        homeNavigationBtn.setOnClickListener{
            homeNavigationBtnClicked(it)
        }
    }

    fun observeLiveData(){

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

        viewModel.favCoins.observe(viewLifecycleOwner, Observer { coins ->

            coins?.let{
                favListRecyclerView.visibility = View.VISIBLE

                if(coins.containsAll(coinList)){
                    coinList.clear()
                }
                coinList.addAll(coins)

                updateRecyclerView()

            }

        })


    }

    fun updateRecyclerView(){
        for(coin in coinList){
            coin.isFavorite = 1
        }
        recyclerCoinAdapter.updateCoinList(coinList)

    }

    fun homeNavigationBtnClicked(view : View){
        val action = FavFragmentDirections.actionFavFragmentToCoinListFragment()
        Navigation.findNavController(view).navigate(action)
    }

    override fun favBtnClick(view: View) {

        view.favBtn.setImageResource(R.drawable.heart)
        /*
        val coinId = view.coinIdTV.text.toString().toInt()
        val coinSym = view.coinSymTV.text.toString()

        Log.d("favBtn", "coin id : $coinId")
        Log.d("favBtn", "coin id : $coinSym")

        val favCoin = FavCoin(coinId, coinSym)

        viewModel.setFavCoin(favCoin)

         */

    }

    override fun coinClick(view: View) {
        val action = FavFragmentDirections.actionFavFragmentToCoinDetailFragment(view.coinId.text.toString().toInt())
        Navigation.findNavController(view).navigate(action)
    }

}