package com.onimaskesi.onicointracker.model

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity
data class Coin(

        @PrimaryKey
        @ColumnInfo(name = "id")
        @SerializedName("id")
        val id : Long,

        @ColumnInfo(name = "name")
        @SerializedName("name")
        val name : String?,

        @ColumnInfo(name = "symbol")
        @SerializedName("symbol")
        val sym : String?,

        @Embedded
        @SerializedName("quote")
        val quote : Quote?,

        @ColumnInfo(name = "isFav")
        var isFavorite : Int?
)