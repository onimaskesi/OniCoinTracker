package com.onimaskesi.onicointracker.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.onimaskesi.onicointracker.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.coin_recycler_raw.view.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(this, R.id.fragment)
        bottomNavigation.setupWithNavController(navController)
    }
}