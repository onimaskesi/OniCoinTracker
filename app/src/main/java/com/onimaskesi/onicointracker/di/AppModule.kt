package com.onimaskesi.onicointracker.di

import android.content.Context
import com.onimaskesi.onicointracker.service.api.CoinApiService
import com.onimaskesi.onicointracker.service.api.FavCoinApiService
import com.onimaskesi.onicointracker.service.database.CoinDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCoinApiService() : CoinApiService = CoinApiService()

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context : Context
    ) : CoinDatabase = CoinDatabase(context)

    @Singleton
    @Provides
    fun provideFavCoinApiService() : FavCoinApiService = FavCoinApiService()

}