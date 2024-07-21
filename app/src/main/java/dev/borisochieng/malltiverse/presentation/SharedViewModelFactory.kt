package dev.borisochieng.malltiverse.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.borisochieng.malltiverse.domain.LocalDatabaseRepository
import dev.borisochieng.malltiverse.domain.TimbuAPIRepository
import dev.borisochieng.malltiverse.presentation.ui.screens.orderhistory.OrderHistoryViewModel
import dev.borisochieng.malltiverse.util.CoroutineDispatcherProvider

class SharedViewModelFactory(
    private val timbuAPIRepository: TimbuAPIRepository,
    private val localDatabaseRepository: LocalDatabaseRepository,
    private val dispatcher: CoroutineDispatcherProvider
) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(MainActivityViewModel::class.java) -> {
                MainActivityViewModel(timbuAPIRepository, dispatcher = dispatcher, localDatabaseRepository = localDatabaseRepository) as T
            }

            modelClass.isAssignableFrom(OrderHistoryViewModel::class.java) -> {
                OrderHistoryViewModel(localDatabaseRepository) as T
            }

            else ->
                throw IllegalArgumentException("Unknown ViewModel class")

        }

}