package com.onimaskesi.onicointracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.onimaskesi.onicointracker.R
import com.onimaskesi.onicointracker.databinding.CoinRecyclerRawBinding
import com.onimaskesi.onicointracker.model.Coin
import com.onimaskesi.onicointracker.util.RoundString
import com.onimaskesi.onicointracker.view.CoinListFragmentDirections
import kotlinx.android.synthetic.main.coin_recycler_raw.view.*


class CoinRecyclerAdapter(val coinList: ArrayList<Coin>, val favBtnClickListener : FavBtnClickListener) : RecyclerView.Adapter<CoinRecyclerAdapter.CoinViewHolder>() , CoinClickListener {


    class CoinViewHolder(var view: CoinRecyclerRawBinding) : RecyclerView.ViewHolder(view.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        //val view = inflater.inflate(R.layout.coin_recycler_raw, parent, false)
        val view = DataBindingUtil.inflate<CoinRecyclerRawBinding>(inflater, R.layout.coin_recycler_raw, parent, false)
        return CoinViewHolder(view)
    }

    override fun getItemCount(): Int {
        return coinList.size
    }



    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {

        holder.view.coin = coinList.get(position)
        holder.view.roundString = RoundString()
        holder.view.coinClickListener = this
        holder.view.favBtnClickListener = favBtnClickListener

    }

    fun updateCoinList(newCoinList: List<Coin>){
        coinList.clear()
        coinList.addAll(newCoinList)
        notifyDataSetChanged()
    }

    override fun coinClick(view: View) {
        val action = CoinListFragmentDirections.actionCoinListFragmentToCoinDetailFragment(view.coinId.text.toString().toInt())
        Navigation.findNavController(view).navigate(action)
    }


}