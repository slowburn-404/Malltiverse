package dev.borisochieng.malltiverse.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.borisochieng.malltiverse.R
import dev.borisochieng.malltiverse.data.local.entities.WishListItem
import dev.borisochieng.malltiverse.domain.models.DomainWishlistItem
import dev.borisochieng.malltiverse.presentation.MainActivityViewModel
import dev.borisochieng.malltiverse.presentation.ui.components.WishlistItemCard
import dev.borisochieng.malltiverse.presentation.ui.theme.MalltiverseTheme
import dev.borisochieng.malltiverse.util.UIEvents

@Composable
fun WishlistScreen(
    viewModel: MainActivityViewModel,
    onCardClick: (DomainWishlistItem) -> Unit,
    snackBarHostState: SnackbarHostState
) {
    LaunchedEffect(Unit) {
        viewModel.getWishlist()
    }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            if (event is UIEvents.SnackBarEvent) {
                snackBarHostState.showSnackbar(event.message)
            }
        }

    }


    if (uiState.wishlist.isNotEmpty()) {
        val wishlist = uiState.wishlist
        val lazyListState = rememberLazyListState()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = lazyListState,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(8.dp)
        ) {
            items(wishlist, key = { it.id }) { item ->
                WishlistItemCard(
                    wishlistItem = item,
                    onRemoveFromWishlistClick = {
                        viewModel.removeFromWishList(item)
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
                text = "No items in wishlist",
                modifier = Modifier
                    .padding(8.dp),
                style = MalltiverseTheme.typography.bodyLarge
            )
        }
    }
}