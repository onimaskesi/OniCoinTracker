package com.onimaskesi.onicointracker.view

import android.content.Context.MODE_PRIVATE
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.res.ResourcesCompat.getColor
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

                dataBinding.chosenCoin = it

            }

        })
    }


}