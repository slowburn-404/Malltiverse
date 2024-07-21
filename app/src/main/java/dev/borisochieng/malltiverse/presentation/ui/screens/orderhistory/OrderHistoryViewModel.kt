package dev.borisochieng.malltiverse.presentation.ui.screens.orderhistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.borisochieng.malltiverse.data.local.DatabaseResponse
import dev.borisochieng.malltiverse.domain.LocalDatabaseRepository
import dev.borisochieng.malltiverse.domain.models.DomainOrder
import dev.borisochieng.malltiverse.domain.toOrder
import dev.borisochieng.malltiverse.util.UIEvents
import dev.borisochieng.malltiverse.util.UIState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderHistoryViewModel(private val localDatabaseRepository: LocalDatabaseRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> get() = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvents>()
    val eventFlow: SharedFlow<UIEvents> get() = _eventFlow.asSharedFlow()


    fun getOrderHistory() =
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = ""
                )
            }

            val orderHistory = localDatabaseRepository.getOrders()

            orderHistory.collect { response ->
                when (response) {
                    is DatabaseResponse.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            orderHistory = response.items ?: emptyList()
                        )

                    }

                    is DatabaseResponse.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = response.message
                        )

                        _uiState.update {
                            it.copy(
                                errorMessage = response.message
                            )
                        }

                        _eventFlow.emit(UIEvents.SnackBarEvent(response.message))
                    }
                }
            }
        }

    fun addToOrderHistory(domainOrder: DomainOrder) =
        viewModelScope.launch {
            when (val addOrder = localDatabaseRepository.addOrder(domainOrder.toOrder())) {
                is DatabaseResponse.Success -> {
                    _eventFlow.emit(UIEvents.SnackBarEvent("Added to order history"))
                }

                is DatabaseResponse.Error -> {

                    _eventFlow.emit(UIEvents.SnackBarEvent(addOrder.message))
                }
            }
        }

    fun removeFromOrderHistory(domainOrder: DomainOrder) =
        viewModelScope.launch {
            when(val removeOrder = localDatabaseRepository.deleteOrder(domainOrder.toOrder())){
                is DatabaseResponse.Success -> {
                    _eventFlow.emit(UIEvents.SnackBarEvent("Removed from order history"))
                }
                is DatabaseResponse.Error -> {
                    _eventFlow.emit(UIEvents.SnackBarEvent(removeOrder.message))
                }
            }
        }
}