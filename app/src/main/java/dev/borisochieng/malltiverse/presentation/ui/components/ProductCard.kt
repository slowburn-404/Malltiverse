package dev.borisochieng.malltiverse.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.borisochieng.malltiverse.R
import dev.borisochieng.malltiverse.domain.models.DomainProduct
import dev.borisochieng.malltiverse.presentation.ui.theme.MalltiverseTheme
import dev.borisochieng.malltiverse.presentation.ui.theme.MalltiverseTheme.colorScheme
import dev.borisochieng.malltiverse.presentation.ui.theme.MalltiverseTheme.shape

@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    product: DomainProduct,
    onAddToCartClick: (DomainProduct) -> Unit,
    onAddToWishlistClick: (DomainProduct) -> Unit
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .height(IntrinsicSize.Min)
            .width(200.dp)
            .clip(shape.card),
        shape = shape.card,
        colors = CardDefaults.cardColors(Color.Transparent),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorScheme.container)
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
                        .data(product.imageURL)
                        .build(),
                    contentDescription = product.name,
                    contentScale = ContentScale.Fit
                )

                IconButton(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp),
                    onClick = {
                        onAddToWishlistClick(product)
                    }
                ) {
                    Icon(
                        imageVector = if (product.isAddedToWishlist) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                        contentDescription = "Add to wishlist",
                        tint = colorScheme.primary
                    )
                }
            }

            //product details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 4.dp, vertical = 4.dp)
                        .fillMaxWidth(),
                    text = product.name,
                    style = MalltiverseTheme.typography.body,
                    color = colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                    text = product.description,
                    style = MalltiverseTheme.typography.bodySmall,
                    color = colorScheme.onBackground,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Image(
                    painter = painterResource(id = R.drawable.stars),
                    contentDescription = "Rating",
                    modifier = Modifier
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                        .align(Alignment.Start)
                )

                Text(
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                    text = "₦ ${product.price}",
                    style = MalltiverseTheme.typography.bodySmall,
                    color = colorScheme.primary
                )
                Spacer(modifier = Modifier.weight(1f))
                AddToCartButton(
                    modifier = Modifier.padding(4.dp),
                    label = if (product.isAddedToCart) {
                        stringResource(id = R.string.remove_from_cart)
                    } else {
                        stringResource(id = R.string.add_to_cart)
                    },
                    onClick = {
                        onAddToCartClick(product)
                    }
                )
            }
        }
    }
}