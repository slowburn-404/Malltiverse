package dev.borisochieng.malltiverse.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.borisochieng.malltiverse.presentation.MainActivityViewModel
import dev.borisochieng.malltiverse.presentation.ui.components.FeaturedCard
import dev.borisochieng.malltiverse.presentation.ui.components.LazyRowWithScrollIndicator
import dev.borisochieng.malltiverse.presentation.ui.theme.MalltiverseTheme.colorScheme
import dev.borisochieng.malltiverse.util.UIEvents

@Composable
fun HomeScreen(
    mainActivityViewModel: MainActivityViewModel,
    snackBarHostState: SnackbarHostState
) {
    val uiState by mainActivityViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(30.dp),
                    color = colorScheme.primary
                )
            }
        } else {
            val categories = uiState.categoriesWithProducts
            val lazyColumnState = rememberLazyListState()
            LazyColumn(
                state = lazyColumnState,
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {

                item {
                    FeaturedCard(modifier = Modifier)
                }
                items(categories.keys.toList(), key = { it } ) { categoryName ->
                    LazyRowWithScrollIndicator(
                        categoryName = categoryName,
                        products = categories[categoryName] ?: emptyList(),
                        viewModel = mainActivityViewModel,
                        onAddToWishlistClick = {
                            mainActivityViewModel.toggleWishlist(it)
                        }
                    )
                }
            }

        }

        if (uiState.errorMessage.isNotEmpty()) {
            LaunchedEffect(Unit) {
                mainActivityViewModel.eventFlow.collect{ event->
                    when(event) {
                        is UIEvents.SnackBarEvent -> {
                            snackBarHostState.showSnackbar(event.message)
                        }

                    }
                }
            }
        }
    }

}