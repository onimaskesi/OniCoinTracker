package com.onimaskesi.onicointracker.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.onimaskesi.onicointracker.R
import com.onimaskesi.onicointracker.databinding.CoinRecyclerRawBinding
import com.onimaskesi.onicointracker.model.coin.Coin
import com.onimaskesi.onicointracker.util.RoundString


class CoinRecyclerAdapter(val coinList: ArrayList<Coin>, val favBtnClickListener : FavBtnClickListener, val coinClickListener: CoinClickListener) : RecyclerView.Adapter<CoinRecyclerAdapter.CoinViewHolder>(){


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
        holder.view.coinClickListener = coinClickListener
        holder.view.favBtnClickListener = favBtnClickListener

    }

    fun updateCoinList(newCoinList: List<Coin>){
        coinList.clear()
        coinList.addAll(newCoinList)
        notifyDataSetChanged()
    }

}