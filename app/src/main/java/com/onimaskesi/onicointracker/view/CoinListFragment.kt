package com.onimaskesi.onicointracker.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onimaskesi.onicointracker.R
import com.onimaskesi.onicointracker.adapter.CoinClickListener
import com.onimaskesi.onicointracker.adapter.CoinRecyclerAdapter
import com.onimaskesi.onicointracker.adapter.FavBtnClickListener
import com.onimaskesi.onicointracker.model.coin.Coin
import com.onimaskesi.onicointracker.model.favcoin.FavCoin
import com.onimaskesi.onicointracker.viewmodel.CoinListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.coin_recycler_raw.view.*
import kotlinx.android.synthetic.main.fragment_coin_list.*

@AndroidEntryPoint
class CoinListFragment : Fragment(), FavBtnClickListener, CoinClickListener {

    private val viewModel : CoinListViewModel by viewModels()
    private var recyclerCoinAdapter = CoinRecyclerAdapter(arrayListOf(),this, this)

    private var isLoading = false

    var coinList : ArrayList<Coin> = arrayListOf()

    var start = 1
    var limit  = 10

    val PREFS_FILENAME = "onimaskesi"
    val KEY = "start"
    lateinit var prefences : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coin_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.refreshData()

        coinListRecyclerView.adapter = recyclerCoinAdapter

        observeLiveData()

        swipeRefreshLayout.setOnRefreshListener {
            coinListProgressBar.visibility = View.VISIBLE
            coinListErrorMessage.visibility = View.GONE
            coinListRecyclerView.visibility = View.GONE

            start = 1
            prefences.edit().putInt(KEY, start).apply()
            coinList.clear()
            viewModel.refreshFromInternet(start, limit)

            swipeRefreshLayout.isRefreshing = false

        }

        initScrollLister()

        this.getActivity()?.let{
            prefences = it.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
            start = prefences.getInt(KEY, 1)
        }

    }

    fun observeLiveData(){

        coinListErrorMessage.visibility = View.INVISIBLE
        coinListProgressBar.visibility = View.VISIBLE

        viewModel.coinLoading.observe(viewLifecycleOwner, Observer{ isLoading ->
            isLoading?.let {
                if(isLoading){
                    coinListProgressBar.visibility = View.VISIBLE
                } else {
                    coinListProgressBar.visibility = View.GONE
                }
            }
        })

        viewModel.coinErrorMessage.observe(viewLifecycleOwner, Observer{ isError ->
            isError?.let {
                if(isError){
                    coinListRecyclerView.visibility = View.GONE
                    coinListErrorMessage.visibility = View.VISIBLE
                } else {
                    coinListErrorMessage.visibility = View.GONE
                }
            }
        })

        viewModel.coins.observe(viewLifecycleOwner, Observer { coins ->

            coins?.let{
                coinListRecyclerView.visibility = View.VISIBLE

                coinList.clear()
                coinList.addAll(coins)

                updateRecyclerView()

                loadingMore(false)
            }

        })

    }

    fun updateRecyclerView(){
        recyclerCoinAdapter.updateCoinList(coinList)
    }

    fun loadingMore(isLoad : Boolean){
        if(isLoad){
            isLoading = true
            loadingMoreProgressBar.visibility = View.VISIBLE
            coinListProgressBar.visibility = View.GONE
        } else {
            isLoading = false
            loadingMoreProgressBar.visibility = View.GONE
        }
    }

    fun initScrollLister(){

        coinListRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == coinList.size - 1) {
                        //bottom of list!
                        loadMore()
                        loadingMore(true)
                    }
                }
            }
        })
    }

    fun loadMore() {

        loadingMoreProgressBar.visibility = View.VISIBLE
        start += limit
        prefences.edit().putInt(KEY, start).apply()
        viewModel.refreshFromInternet(start, limit)

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

        clickedCoin?.let {

            if(clickedCoin.isFavorite != 1){ // if the coin is not favorite

                clickedCoin.isFavorite = 1

                val favCoin = FavCoin(coinId, clickedCoin.sym!!)

                viewModel.setFavCoin(favCoin)

                updateRecyclerView()

            }

        }



    }

    override fun coinClick(view: View) {

        val action = CoinListFragmentDirections.actionCoinListFragmentToCoinDetailFragment(view.coinId.text.toString().toInt())
        Navigation.findNavController(view).navigate(action)
    }

}