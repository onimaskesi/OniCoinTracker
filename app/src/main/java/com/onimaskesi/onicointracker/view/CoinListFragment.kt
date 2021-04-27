package com.onimaskesi.onicointracker.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onimaskesi.onicointracker.R
import com.onimaskesi.onicointracker.adapter.CoinClickListener
import com.onimaskesi.onicointracker.adapter.CoinRecyclerAdapter
import com.onimaskesi.onicointracker.adapter.FavBtnClickListener
import com.onimaskesi.onicointracker.model.Coin
import com.onimaskesi.onicointracker.model.FavCoin
import com.onimaskesi.onicointracker.viewmodel.CoinListViewModel
import kotlinx.android.synthetic.main.coin_recycler_raw.view.*
import kotlinx.android.synthetic.main.fragment_coin_list.*
import java.math.BigDecimal
import java.math.RoundingMode

class CoinListFragment : Fragment(), FavBtnClickListener, CoinClickListener {

    private lateinit var viewModel : CoinListViewModel
    private var recyclerCoinAdapter = CoinRecyclerAdapter(arrayListOf(),this, this)

    private var isLoading = false

    var coinList : ArrayList<Coin> = arrayListOf()
    var isFavList : ArrayList<Boolean> = arrayListOf()

    var start = 1
    var limit  = 10

    val PREFS_FILENAME = "onimaskesi"
    val KEY = "start"
    lateinit var prefences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coin_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(CoinListViewModel::class.java)
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

        favNavigationBtn.setOnClickListener {
            favNavigationBtnClicked(it)
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

                if(coins.containsAll(coinList)){
                    coinList.clear()
                }
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

        //view.favBtn.setImageResource(R.drawable.filled_heart)
        val coinId = view.coinIdTV.text.toString().toInt()
        val coinSym = view.coinSymTV.text.toString()
        for (coin in coinList){
            if(coin.id.toInt() == coinId){
                coin.isFavorite = 1
            }
        }

        val favCoin = FavCoin(coinId, coinSym)

        viewModel.setFavCoin(favCoin)

        updateRecyclerView()

    }

    fun favNavigationBtnClicked(view : View){
        val action = CoinListFragmentDirections.actionCoinListFragmentToFavFragment()
        Navigation.findNavController(view).navigate(action)
    }

    override fun coinClick(view: View) {

        val action = CoinListFragmentDirections.actionCoinListFragmentToCoinDetailFragment(view.coinId.text.toString().toInt())
        Navigation.findNavController(view).navigate(action)
    }

}