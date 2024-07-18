package dev.borisochieng.malltiverse

import android.app.Application
import dev.borisochieng.malltiverse.data.repository.TimbuAPIRepositoryImpl
import dev.borisochieng.malltiverse.data.retrofit.RetrofitClient
import dev.borisochieng.malltiverse.data.service.TimbuAPIService
import dev.borisochieng.malltiverse.util.CoroutineDispatcherProvider
import kotlinx.coroutines.Dispatchers

class MalltiverseApplication : Application() {
    lateinit var dispatcherProvider: CoroutineDispatcherProvider
    override fun onCreate() {
        super.onCreate()
        dispatcherProvider = CoroutineDispatcherProvider

    }

    val timbuAPIRepositoryImpl: TimbuAPIRepositoryImpl by lazy {
        TimbuAPIRepositoryImpl(
            RetrofitClient.instance.create(TimbuAPIService::class.java),
            dispatcherProvider
        )
    }


}