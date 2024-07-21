package dev.borisochieng.malltiverse.presentation.ui.screens.cart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.borisochieng.malltiverse.R
import dev.borisochieng.malltiverse.presentation.MainActivityViewModel
import dev.borisochieng.malltiverse.presentation.ui.components.CartItem
import dev.borisochieng.malltiverse.presentation.ui.components.ShoppingSummary
import dev.borisochieng.malltiverse.presentation.ui.theme.MalltiverseTheme

@Composable
fun CartScreen(
    onCheckoutClick: () -> Unit,
    viewModel: MainActivityViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val cartProducts = uiState.cartItems

    var promoCode by remember { mutableStateOf("") }

    val deliveryFee by remember { mutableDoubleStateOf(1500.00) }
    val discount by remember { mutableDoubleStateOf(1000.00) }
    val totalAmount  = viewModel.getTotalCartPrice()


    if(cartProducts.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            items(cartProducts) { cartItem ->
                CartItem(
                    product = cartItem,
                    onRemoveFromCart = {
                        viewModel.toggleCart(cartItem)
                    },
                    onQuantityChange = { product, newQuantity ->
                        viewModel.updateQuantity(product, newQuantity)
                        //totalAmount = cartItem.price + deliveryFee - discount
                    }
                )

            }

            item {
                ShoppingSummary(
                    modifier = Modifier.padding(8.dp),
                    promoCode = promoCode,
                    subTotal = totalAmount,
                    deliveryFee = deliveryFee,
                    discount = discount,
                    onValueChange = { promoCode = it },
                    onClick = onCheckoutClick
                )
            }

        }
    }
    else{
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center

        ) {
            Text(
                text = stringResource(R.string.no_items_in_cart),
                modifier = Modifier
                    .padding(8.dp),
                style = MalltiverseTheme.typography.bodyLarge
            )
        }
    }
}