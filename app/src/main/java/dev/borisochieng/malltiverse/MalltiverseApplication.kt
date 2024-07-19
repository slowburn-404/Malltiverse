package dev.borisochieng.malltiverse

import android.app.Application
import dev.borisochieng.malltiverse.data.local.database.MalltiverseDatabase
import dev.borisochieng.malltiverse.data.repository.TimbuAPIRepositoryImpl
import dev.borisochieng.malltiverse.data.remote.retrofit.RetrofitClient
import dev.borisochieng.malltiverse.data.remote.service.TimbuAPIService
import dev.borisochieng.malltiverse.data.repository.LocalDatabaseRepositoryImpl
import dev.borisochieng.malltiverse.util.CoroutineDispatcherProvider

class MalltiverseApplication : Application() {
    lateinit var dispatcherProvider: CoroutineDispatcherProvider
    override fun onCreate() {
        super.onCreate()
        dispatcherProvider = CoroutineDispatcherProvider

    }

    //initialise expensive instances to be retrieved only when needed

    val timbuAPIRepositoryImpl: TimbuAPIRepositoryImpl by lazy {
        TimbuAPIRepositoryImpl(
            apiService = RetrofitClient.instance.create(TimbuAPIService::class.java),
            dispatcher = dispatcherProvider
        )
    }

    private val malltiverseDatabase by lazy {
        MalltiverseDatabase.getDatabase(context = this)
    }

    val localDatabaseRepository by lazy {
        LocalDatabaseRepositoryImpl(
            dispatcher = dispatcherProvider,
            orderHistoryDao = malltiverseDatabase.orderHistoryDao(),
            wishListDao = malltiverseDatabase.wishListDao()
        )
    }


}