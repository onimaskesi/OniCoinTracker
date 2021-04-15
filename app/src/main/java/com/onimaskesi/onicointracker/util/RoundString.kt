package com.onimaskesi.onicointracker.util

import java.math.BigDecimal
import java.math.RoundingMode

class RoundString {

    fun roundStringFun(value : String, scale : Int) : String {
        val valueDouble = value.toDouble()
        val decimal = BigDecimal(valueDouble).setScale(scale, RoundingMode.HALF_EVEN)
        return decimal.toString()
    }

}