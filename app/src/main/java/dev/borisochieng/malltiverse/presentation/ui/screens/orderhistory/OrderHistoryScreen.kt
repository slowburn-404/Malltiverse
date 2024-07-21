package dev.borisochieng.malltiverse.presentation.ui.screens.orderhistory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.borisochieng.malltiverse.R
import dev.borisochieng.malltiverse.domain.models.DomainOrder
import dev.borisochieng.malltiverse.presentation.ui.components.OrderItem
import dev.borisochieng.malltiverse.presentation.ui.theme.MalltiverseTheme
import dev.borisochieng.malltiverse.util.UIEvents

@Composable
fun OrderHistoryScreen(
    viewModel: OrderHistoryViewModel,
    snackBarHostState: SnackbarHostState,
    onCardClick: (DomainOrder) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getOrderHistory()
    }


    if (uiState.orderHistory.isNotEmpty()) {
        val orders = uiState.orderHistory
        val lazyListState = rememberLazyListState()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = lazyListState,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(8.dp)
        ) {
            items(orders, key = { it.id }) { order ->
                OrderItem(
                    order = order,
                    onRemoveFromOrderHistoryClick = {
                        viewModel.removeFromOrderHistory(order)
                    },
                    onCardClick = onCardClick
                )
            }

        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center

        ) {
            Text(
                text = stringResource(R.string.no_history),
                modifier = Modifier
                    .padding(8.dp),
                style = MalltiverseTheme.typography.bodyLarge
            )
        }
    }

    if (uiState.errorMessage.isNotEmpty()) {
        LaunchedEffect(Unit) {
            viewModel.eventFlow.collect { event ->
                if (event is UIEvents.SnackBarEvent) {
                    snackBarHostState.showSnackbar(event.message)
                }
            }

        }
    }
}