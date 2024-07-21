package dev.borisochieng.malltiverse.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.borisochieng.malltiverse.R
import dev.borisochieng.malltiverse.presentation.ui.components.PaymentCard
import dev.borisochieng.malltiverse.presentation.ui.theme.MalltiverseTheme
import dev.borisochieng.malltiverse.presentation.ui.theme.MalltiverseTheme.shape

@Composable
fun PaymentScreen(
    onMakePaymentClick: () -> Unit
) {
    var cardNumber by remember { mutableStateOf("") }
    var cardNumberError by remember { mutableStateOf<String?>(null) }
    var expiryDate by remember { mutableStateOf("") }
    var expiryDateError by remember { mutableStateOf<String?>(null) }
    var cvv by remember { mutableStateOf("") }
    var cvvError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        PaymentCard(
            modifier = Modifier,
            cardNumber = cardNumber,
            expiryDate = expiryDate,
        )

        Text(
            text = stringResource(R.string.card_number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, bottom = 4.dp, end = 16.dp),
            style = MalltiverseTheme.typography.body,
            fontWeight = FontWeight.SemiBold
        )

        OutlinedTextField(
            value = cardNumber.chunked(4).joinToString(" "),
            onValueChange = {
                if (it.replace(" ", "").length <= 16) {
                    cardNumber = it.replace(" ", "")
                    cardNumberError = validateCardNumber(cardNumber)
                }
            },
            shape = shape.button,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MalltiverseTheme.colorScheme.background,
                unfocusedContainerColor = MalltiverseTheme.colorScheme.background,
                focusedIndicatorColor = MalltiverseTheme.colorScheme.primary,
                cursorColor = MalltiverseTheme.colorScheme.primary,
            ),
            placeholder = {
                Text(
                    text = "0000 0000 0000 0000",
                    style = MalltiverseTheme.typography.body,
                    color = Color.LightGray
                )
            },
            isError = cardNumberError != null,
            supportingText = {
                if (cardNumberError != null) {
                    Text(
                        text = cardNumberError ?: "",
                        color = Color.Red,
                        style = MalltiverseTheme.typography.labelSmall,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        )

        Row {
            Column {
                Text(
                    text = stringResource(R.string.expiry_date),
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp, bottom = 4.dp, end = 16.dp),
                    style = MalltiverseTheme.typography.body,
                    fontWeight = FontWeight.SemiBold
                )

                OutlinedTextField(
                    value = expiryDate,
                    onValueChange = {
                        expiryDate = formatExpiryDate(it)
                        expiryDateError = validateExpiryDate(expiryDate)
                    },
                    shape = shape.button,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .width(180.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MalltiverseTheme.colorScheme.background,
                        unfocusedContainerColor = MalltiverseTheme.colorScheme.background,
                        focusedIndicatorColor = MalltiverseTheme.colorScheme.primary,
                        cursorColor = MalltiverseTheme.colorScheme.primary,
                    ),
                    placeholder = {
                        Text(
                            text = stringResource(R.string.expiry_date_placeholder),
                            style = MalltiverseTheme.typography.body,
                            color = Color.LightGray
                        )
                    },
                    isError = expiryDateError != null,
                    supportingText = {
                        if (expiryDateError != null) {
                            Text(
                                text = expiryDateError ?: "",
                                color = Color.Red,
                                style = MalltiverseTheme.typography.labelSmall,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                )
            }

            Column {
                Text(
                    text = stringResource(R.string.cvv),
                    modifier = Modifier//
                        .padding(start = 16.dp, top = 16.dp, bottom = 4.dp, end = 16.dp),
                    style = MalltiverseTheme.typography.body,
                    fontWeight = FontWeight.SemiBold
                )

                OutlinedTextField(
                    value = cvv,
                    onValueChange = {
                        if (it.length <= 3) {
                            cvv = it
                            cvvError = validateCVV(cvv)
                        }
                    },
                    shape = shape.button,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .width(180.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MalltiverseTheme.colorScheme.background,
                        unfocusedContainerColor = MalltiverseTheme.colorScheme.background,
                        focusedIndicatorColor = MalltiverseTheme.colorScheme.primary,
                        cursorColor = MalltiverseTheme.colorScheme.primary,
                    ),
                    placeholder = {
                        Text(
                            text = stringResource(R.string.cvv_place_holder),
                            style = MalltiverseTheme.typography.body,
                            color = Color.LightGray
                        )
                    },
                    isError = cvvError != null,
                    supportingText = {
                        if (cvvError != null) {
                            Text(
                                text = cvvError ?: "",
                                color = Color.Red,
                                style = MalltiverseTheme.typography.labelSmall,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(50.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            onClick = onMakePaymentClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MalltiverseTheme.colorScheme.primary,
                contentColor = MalltiverseTheme.colorScheme.onPrimary
            ),
            shape = shape.button,
            enabled = cardNumberError == null && expiryDateError == null && cvvError == null &&
                cardNumber.isNotEmpty() && expiryDate.isNotEmpty() && cvv.isNotEmpty()
        ) {
            Text(
                text = stringResource(R.string.make_payment)
            )
        }
    }
}

private fun validateCardNumber(input: String): String? {
    return when {
        input.isEmpty() -> "Card number cannot be empty"
        input.length != 16 -> "Card number must be 16 digits"
        else -> null
    }
}

private fun validateExpiryDate(input: String): String? {
    return when {
        input.isEmpty() -> "Expiry date cannot be empty"
        !input.matches(Regex("(0[1-9]|1[0-2])/\\d{2}")) -> "Invalid expiry date format (MM/YY)"
        else -> null
    }
}

private fun validateCVV(input: String): String? {
    return when {
        input.isEmpty() -> "CVV cannot be empty"
        input.length != 3 -> "CVV must be 3 digits"
        else -> null
    }
}



fun formatExpiryDate(date: String): String {
    //allow only digits and slash
    val formattedDate = date.replace(Regex("[^0-9/]"), "")
    //add slash after the second digit
    val finalDate = when {
        formattedDate.length == 2 && !formattedDate.contains("/") -> "$formattedDate/"
        formattedDate.length > 5 -> formattedDate.substring(0, 5)
        else -> formattedDate
    }

    return finalDate

}

@Preview(showBackground = true)
@Composable
fun PaymentScreenPreview() {
    PaymentScreen(
        onMakePaymentClick = {}
    )
}