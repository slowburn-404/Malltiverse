package dev.borisochieng.malltiverse.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.borisochieng.malltiverse.domain.models.DomainProduct
import dev.borisochieng.malltiverse.domain.models.DomainWishlistItem
import dev.borisochieng.malltiverse.domain.toDomainWishlistItem
import dev.borisochieng.malltiverse.domain.toWishListItem
import dev.borisochieng.malltiverse.presentation.MainActivityViewModel
import dev.borisochieng.malltiverse.presentation.ui.theme.MalltiverseTheme

@Composable
fun LazyRowWithScrollIndicator(
    products: List<DomainProduct>,
    viewModel: MainActivityViewModel,
    categoryName: String,
    onAddToWishlistClick: (DomainWishlistItem) -> Unit
) {
    val lazyListState = rememberLazyListState()

    val coroutineScope = rememberCoroutineScope()

    val scrollProgress = remember {
        mutableStateOf(0f)
    }


    //update scroll progress based on lazy list changes
    LaunchedEffect(
        lazyListState,
        coroutineScope
    ) {
        snapshotFlow { lazyListState.firstVisibleItemScrollOffset to lazyListState.firstVisibleItemIndex }
            .collect { (offset, index) ->
                val totalItems = lazyListState.layoutInfo.totalItemsCount

                if (totalItems > 0) {
                    val visibleItems = lazyListState.layoutInfo.visibleItemsInfo
                    val totalVisibleItemWidth = visibleItems.sumOf { it.size }.toFloat()
                    val totalScrollableWidth =
                        totalVisibleItemWidth * totalItems / visibleItems.size
                    scrollProgress.value =
                        (index * totalVisibleItemWidth + offset) / totalScrollableWidth
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Text(
            text = categoryName,
            modifier = Modifier
                .fillMaxWidth(),
            style = MalltiverseTheme.typography.titleLarge
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            state = lazyListState
        ) {
            items(products, key = { it.id }) { product ->
                ProductCard(
                    modifier = Modifier,
                    product = product,
                    onAddToCartClick = {
                            viewModel.toggleCart(product)
                    },
                    onAddToWishlistClick = {
                        onAddToWishlistClick(product.toDomainWishlistItem())
                    }
                )
            }
        }
    }

    ScrollIndicator(scrollProgress = scrollProgress.value, totalItems = products.size)
}
