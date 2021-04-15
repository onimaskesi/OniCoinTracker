package com.onimaskesi.onicointracker.util

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings.Global.putLong
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class TimeCheckSharedPreferences {
    
    companion object {
        
        private var sharedPreferences: SharedPreferences? = null
        
        @Volatile private var instance: TimeCheckSharedPreferences? = null
        private val lock = Any()
        operator fun invoke(context: Context) : TimeCheckSharedPreferences = instance ?: synchronized(lock){
            instance ?: createTimeCheckSharedPreferences(context).also {
                
                instance = it
                
            }
        }
        
        private fun createTimeCheckSharedPreferences(context: Context) : TimeCheckSharedPreferences{
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return TimeCheckSharedPreferences()
        }
        
    }

    fun saveTime(time : Long){
        sharedPreferences?.edit(commit = true){
            putLong("time", time)
        }
    }

    fun getTime() = sharedPreferences?.getLong("time",0)
    
}
