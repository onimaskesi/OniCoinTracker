package com.onimaskesi.onicointracker.model

import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

data class Quote(

        @Embedded
        @SerializedName("USD")
        val usd : USD
)