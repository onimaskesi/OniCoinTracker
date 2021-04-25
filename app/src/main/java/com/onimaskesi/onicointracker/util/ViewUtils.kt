package com.onimaskesi.onicointracker.util

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.onimaskesi.onicointracker.R
import java.math.BigDecimal
import java.math.RoundingMode

fun Context.toast(message : String){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun ImageView.downloadImage(url : String, placeholder : CircularProgressDrawable){

    val options =
            RequestOptions()
                    .placeholder(placeholder)
                    .error(R.drawable.ic_launcher_foreground)

    Glide.with(this)
            .setDefaultRequestOptions(options)
            .load(url)
            .into(this)

}

fun createPlaceholder(context : Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply{
        strokeWidth = 8f
        centerRadius = 40f
        start()
    }
}

@BindingAdapter("android:downloadImageFromCoinSymbol")
fun downloadImageFromCoinSymbol(view : ImageView, symbol : String){
    /*
    val base_url_start = "https://cryptoicons.org/api/icon/"
    val base_url_end = "/200.png"
     */
    val base_url_start = "https://icons.bitbot.tools/api/"
    val base_url_end = "/128x128"

    val url = base_url_start + symbol.toLowerCase() + base_url_end
    view.downloadImage(url, createPlaceholder(view.context))
}

@BindingAdapter("android:downloadImageFromUrl")
fun downloadImageFromUrl(view : ImageView, url : String){
    view.downloadImage(url, createPlaceholder(view.context))
}

@BindingAdapter("app:setTextWithColor")
fun setTextWithColor(view: TextView, percent : String?){

    percent?.let {

        if(percent.get(0) == '-'){
            view.setTextColor(Color.RED)
        }
        else {
            view.setTextColor(Color.GREEN)
        }

        view.setText(percent + "%")

    }
}

@BindingAdapter("bind:isFav")
fun isFav(view : ImageView, isFavorite : Boolean){

    if(isFavorite){
        view.setImageResource(R.drawable.filled_heart)
    } else {
        view.setImageResource(R.drawable.heart)
    }

}

/*
fun roundStringFun(value : String) : String {
        val valueDouble = value.toDouble()
        val decimal = BigDecimal(valueDouble).setScale(2, RoundingMode.HALF_EVEN)
        return decimal.toString()
    }
* */
