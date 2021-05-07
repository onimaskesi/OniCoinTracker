package com.onimaskesi.onicointracker.model.favcoin

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavCoin(
        @PrimaryKey
        @ColumnInfo(name = "id")
        val id : Int,

        @ColumnInfo(name = "sym")
        val sym : String
)