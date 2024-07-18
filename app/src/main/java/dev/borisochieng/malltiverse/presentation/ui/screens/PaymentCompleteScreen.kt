package dev.borisochieng.malltiverse.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.borisochieng.malltiverse.R
import dev.borisochieng.malltiverse.presentation.ui.theme.MalltiverseTheme
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import dev.borisochieng.malltiverse.util.CoroutineDispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun PaymentSuccessfulScreen(
    onPaymentComplete: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(timeMillis = 3000L)
        withContext(CoroutineDispatcherProvider.Main) {
            onPaymentComplete()
        }
    }
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.confetti)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1
    )

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            composition = composition,
            progress = { progress }
        )

        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .background(
                        color = MalltiverseTheme.colorScheme.primary,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(100.dp),
                    imageVector = Icons.Rounded.Check,
                    tint = Color.White,
                    contentDescription = stringResource(R.string.order_confirmed),
                )
            }
            Text(
                modifier = Modifier.padding(4.dp),
                text = stringResource(R.string.payment_successful),
                style = MalltiverseTheme.typography.bodyLarge
            )
            Text(
                modifier = Modifier.padding(4.dp),
                text = stringResource(R.string.thank_you_for_your_purchase),
                style = MalltiverseTheme.typography.body,

                )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PaymentSuccessfulScreenPreview() {
    PaymentSuccessfulScreen(
        onPaymentComplete = {}
    )
}