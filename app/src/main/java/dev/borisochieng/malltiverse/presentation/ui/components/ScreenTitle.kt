package dev.borisochieng.malltiverse.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.borisochieng.malltiverse.R
import dev.borisochieng.malltiverse.presentation.ui.theme.MalltiverseTheme
import dev.borisochieng.malltiverse.presentation.ui.theme.MalltiverseTheme.typography

@Composable
fun ScreenTitle(
    title: String,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
            .background(MalltiverseTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(horizontal = 4.dp)
        )

        Text(
            text = title,
            style = typography.titleLarge,
            modifier = Modifier
                .align(Alignment.Center),
//                .wrapContentWidth(
//                    align = Alignment.CenterHorizontally
//                ),
            textAlign = TextAlign.Center
        )
    }


}

@Preview
@Composable
fun ScreenTitlePreview() {
    ScreenTitle(title = "Products List")

}