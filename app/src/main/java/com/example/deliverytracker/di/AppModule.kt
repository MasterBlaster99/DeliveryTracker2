package com.example.deliverytracker.di

import android.content.Context
import androidx.room.Room
import com.example.deliverytracker.data.OrdersApi
import com.example.deliverytracker.data.room.AppDatabase
import com.example.deliverytracker.data.room.OrderDao
import com.example.deliverytracker.domain.AppRepository
import com.example.deliverytracker.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiService(): OrdersApi =
        Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build().create(OrdersApi::class.java)

    @Provides
    @Singleton
    fun provideMoviesRepo(api: OrdersApi,orderDao: OrderDao): AppRepository = AppRepository(api,orderDao)

    @Provides
    fun provideOrderDao(appDatabase: AppDatabase): OrderDao = appDatabase.entityDao()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(appContext, AppDatabase::class.java, "Delivery.db").build()

}