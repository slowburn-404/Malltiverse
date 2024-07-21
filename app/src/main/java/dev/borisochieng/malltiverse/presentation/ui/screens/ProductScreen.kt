package dev.borisochieng.malltiverse.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.borisochieng.malltiverse.R
import dev.borisochieng.malltiverse.domain.models.DomainProduct
import dev.borisochieng.malltiverse.presentation.MainActivityViewModel
import dev.borisochieng.malltiverse.presentation.ui.theme.MalltiverseTheme
import dev.borisochieng.malltiverse.presentation.ui.theme.MalltiverseTheme.colorScheme
import dev.borisochieng.malltiverse.presentation.ui.theme.MalltiverseTheme.shape
import dev.borisochieng.malltiverse.util.Constants.API_KEY
import dev.borisochieng.malltiverse.util.Constants.APP_ID
import dev.borisochieng.malltiverse.util.Constants.ORGANIZATION_ID
import dev.borisochieng.malltiverse.util.UIEvents

@Composable
fun ProductScreen(
    productID: String,
    viewModel: MainActivityViewModel,
    snackBarHostState: SnackbarHostState
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getProduct(
            productId = productID,
            apiKey = API_KEY,
            organizationID = ORGANIZATION_ID,
            appId = APP_ID
        )
    }

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
        val product = uiState.product
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorScheme.background)
                    .clip(shape.card)
                    .height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    modifier = Modifier
                        .padding(6.dp)
                        .size(100.dp),
                    model = ImageRequest
                        .Builder(LocalContext.current)
                        .data(product?.imageURL)
                        .build(),
                    contentDescription = product?.name,
                    contentScale = ContentScale.Fit
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = product?.name ?: "",
                    style = MalltiverseTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )

                Text(
                    text = "${product?.price ?: 0.0}",
                    style = MalltiverseTheme.typography.body,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
                Text(
                    text = "In stock(${product?.availableQuantity ?: 0})",
                    style = MalltiverseTheme.typography.body,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
                Text(
                    text = stringResource(R.string.description),
                    style = MalltiverseTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Text(
                    text = product?.description ?: "",
                    style = MalltiverseTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    modifier = Modifier
                        .padding(16.dp)
                        .height(50.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    onClick = {
                        product?.let {
                            viewModel.toggleCart(it)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.primary,
                        contentColor = colorScheme.onPrimary
                    ),
                    shape = shape.button
                ) {

                    Text(
                        text = stringResource(R.string.add_to_cart)
                    )
                }



            }

        }

        if (uiState.errorMessage.isNotEmpty()) {
            LaunchedEffect(Unit) {
                viewModel.eventFlow.collect{ event->
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