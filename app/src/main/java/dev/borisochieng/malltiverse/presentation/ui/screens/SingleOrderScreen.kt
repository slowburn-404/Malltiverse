package dev.borisochieng.malltiverse.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.borisochieng.malltiverse.R
import dev.borisochieng.malltiverse.domain.models.DomainOrder
import dev.borisochieng.malltiverse.presentation.MainActivityViewModel
import dev.borisochieng.malltiverse.presentation.ui.theme.MalltiverseTheme
import dev.borisochieng.malltiverse.presentation.ui.theme.MalltiverseTheme.colorScheme
import dev.borisochieng.malltiverse.presentation.ui.theme.MalltiverseTheme.shape
import java.util.Date

@Composable
fun SingleOrderScreen(
    orderId: String,
    viewModel: MainActivityViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getOrderById(orderId)
    }

    val order = uiState.order

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

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
                        .align(Alignment.Center)
                        .padding(8.dp)
                        .fillMaxSize(),
                    model = ImageRequest
                        .Builder(LocalContext.current)
                        .data(order?.imageUrl)
                        .build(),
                    contentDescription = order?.name,
                    contentScale = ContentScale.Fit
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = if (order?.status == 1) "Completed" else "Failed",
                        style = MalltiverseTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        color = if (order?.status == 1) Color.Green else Color.Red
                    )

                    order?.timeStamp?.let {
                        Text(
                            text = it,
                            style = MalltiverseTheme.typography.bodySmall,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                            color = Color.Gray
                        )
                    }
                }

                if (order != null) {
                    Text(
                        text = order.name,
                        style = MalltiverseTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis

                    )
                }

                Text(
                    text = "₦ ${order?.price}",
                    style = MalltiverseTheme.typography.body,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    color = colorScheme.primary
                )

                Text(
                    text = stringResource(R.string.description),
                    style = MalltiverseTheme.typography.body,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontWeight = FontWeight.SemiBold
                )
                order?.description?.let {
                    Text(
                        text = it,
                        style = MalltiverseTheme.typography.body,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
//
//                Spacer(modifier = Modifier.weight(1f))
//
//                Button(
//                    modifier = Modifier
//                        .padding(16.dp)
//                        .height(50.dp)
//                        .fillMaxWidth()
//                        .align(Alignment.CenterHorizontally),
//                    onClick = {
//                        product?.let {
//                            viewModel.toggleCart(it)
//                        }
//                    },
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = colorScheme.primary,
//                        contentColor = colorScheme.onPrimary
//                    ),
//                    shape = shape.button
//                ) {
//
//                    Text(
//                        text = stringResource(R.string.add_to_cart)
//                    )
//                }


            }

        }

    }
}