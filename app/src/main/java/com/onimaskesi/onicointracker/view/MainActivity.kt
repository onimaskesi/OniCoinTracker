package com.onimaskesi.onicointracker.view

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.huawei.hms.ads.AdListener
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.BannerAdSize
import com.huawei.hms.ads.InterstitialAd
import com.huawei.hms.ads.banner.BannerView
import com.huawei.hms.push.HmsMessaging
import com.onimaskesi.onicointracker.R
import com.onimaskesi.onicointracker.service.notification.NotificationWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.time.Duration

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    var interstitialAd = InterstitialAd(this)

    lateinit var preferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(this, R.id.fragment)
        bottomNavigation.setupWithNavController(navController)

        val hmsMessaging = HmsMessaging.getInstance(this)
        hmsMessaging.isAutoInitEnabled = true

        preferences = getSharedPreferences("workManagerPref", MODE_PRIVATE)

        if(!preferences.getBoolean("isNotificationWorkerExist", false)){
            createNotificationWorker()
            preferences.edit().putBoolean("isNotificationWorkerExist", true).apply()
        }

        createBannerAd()
        createInterstitialAd()

    }

    private fun createNotificationWorker(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val workRequest : WorkRequest =
            PeriodicWorkRequestBuilder<NotificationWorker>(Duration.ofHours(12))
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(this).enqueue(workRequest)
    }


    fun createInterstitialAd(){
        interstitialAd.adId = getString(R.string.INTERSTITIAL_AD_ID)
        loadInterstitialAd()
    }

    private fun loadInterstitialAd() {
        // Load an interstitial ad.
        val adParam = AdParam.Builder().build()
        interstitialAd.loadAd(adParam)
        interstitialAd.adListener = interstitialAdListener

    }

    private fun showInterstitialAd() {
        // Display the ad.
        if (interstitialAd != null && interstitialAd!!.isLoaded) {
            interstitialAd!!.show(this)
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show()
        }
    }

    private val interstitialAdListener: AdListener = object : AdListener() {
        override fun onAdLoaded() {
            // Called when an ad is loaded successfully.
            showInterstitialAd()
        }
        override fun onAdFailed(errorCode: Int) {
            // Called when an ad fails to be loaded.

        }
        override fun onAdClosed() {
            // Called when an ad is closed.

        }
        override fun onAdClicked() {
            // Called when an ad is clicked.

        }
        override fun onAdLeave() {
            // Called when an ad leaves an app.

        }
        override fun onAdOpened() {
            // Called when an ad is opened.

        }
        override fun onAdImpression() {
            // Called when an ad impression occurs.

        }
    }

    fun createBannerAd(){
        // Obtain BannerView.
        val bannerView: BannerView? = findViewById(R.id.hw_banner_view)
        bannerView?.let{
            // Set the ad unit ID and ad dimensions. "testw6vs28auh3" is a dedicated test ad unit ID.
            bannerView.adId = getString(R.string.BANNER_AD_ID)
            bannerView.bannerAdSize = BannerAdSize.BANNER_SIZE_360_57
            // Set the refresh interval to 30 seconds.
            bannerView.setBannerRefresh(30)
            // Create an ad request to load an ad.
            val adParam = AdParam.Builder().build()
            bannerView.loadAd(adParam)

            bannerView.adListener = bannerAdListener
        }

    }

    private val bannerAdListener: AdListener = object : AdListener() {
        override fun onAdLoaded() {
            // Called when an ad is loaded successfully.

        }
        override fun onAdFailed(p0: Int) {
            // Called when an ad fails to be loaded.

        }
        override fun onAdOpened() {
            // Called when an ad is opened.

        }
        override fun onAdClicked() {
            // Called when an ad is clicked.

        }
        override fun onAdLeave() {
            // Called when an ad leaves an app.

        }
        override fun onAdClosed() {
            // Called when an ad is closed.

        }
    }

}