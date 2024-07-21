package dev.borisochieng.malltiverse.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.borisochieng.malltiverse.R
import dev.borisochieng.malltiverse.domain.models.DomainWishlistItem
import dev.borisochieng.malltiverse.presentation.ui.theme.MalltiverseTheme
import dev.borisochieng.malltiverse.presentation.ui.theme.MalltiverseTheme.shape

@Composable
fun WishlistItemCard(wishlistItem: DomainWishlistItem, onRemoveFromWishlistClick: (DomainWishlistItem) -> Unit, onCardClick: (DomainWishlistItem) -> Unit) {
    Card(
        modifier = Modifier.
        padding(8.dp)
            .fillMaxWidth()
            .clickable { onCardClick(wishlistItem) },
        colors = CardDefaults.cardColors(Color.Transparent),
        shape = shape.container,
        border = BorderStroke(2.dp, Color.LightGray)
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                modifier = Modifier
                    .padding(8.dp)
                    .size(50.dp)
                    .align(Alignment.CenterVertically),
                model = ImageRequest
                    .Builder(LocalContext.current)
                    .data(wishlistItem.imageURL)
                    .build(),
                contentDescription = wishlistItem.name,
                contentScale = ContentScale.Fit
            )

            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Column {
                        Text(
                            modifier = Modifier
                                .padding(4.dp),
                            text = wishlistItem.name,
                            style = MalltiverseTheme.typography.body,
                            fontWeight = FontWeight.SemiBold,
                        )

                        Text(
                            modifier = Modifier
                                .padding(4.dp),
                            text = wishlistItem.description,
                            style = MalltiverseTheme.typography.bodySmall,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        modifier = Modifier
                            .size(24.dp)
                            .padding(4.dp),
                        onClick = {
                            onRemoveFromWishlistClick(wishlistItem)
                        },
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.LightGray
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_trash),
                            contentDescription = "Remove from cart",
                        )
                    }
                }
            }
        }
    }
}