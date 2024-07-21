package dev.borisochieng.malltiverse.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.borisochieng.malltiverse.R
import dev.borisochieng.malltiverse.presentation.ui.theme.MalltiverseTheme
import dev.borisochieng.malltiverse.presentation.ui.theme.MalltiverseTheme.shape

@Composable
fun CheckoutScreen(
    onGoToPaymentClick: () -> Unit
) {
    var selected1 by remember { mutableStateOf(true) }
    var selected2 by remember { mutableStateOf(false) }

    var delivery by remember { mutableStateOf("") }
    var deliveryError by remember { mutableStateOf<String?>(null) }
    var contact1 by remember { mutableStateOf("") }
    var contact1Error by remember { mutableStateOf<String?>(null) }
    var contact2 by remember { mutableStateOf("") }
    var contact2Error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {

        Text(
            text = stringResource(R.string.select_packages),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            style = MalltiverseTheme.typography.bodyLarge
        )

        Text(
            text = stringResource(R.string.pickup),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            style = MalltiverseTheme.typography.body,
            fontWeight = FontWeight.SemiBold
        )

        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .fillMaxWidth()
        ) {
            RadioButton(
                modifier = Modifier
                    .size(24.dp),
                selected = selected1,
                onClick = { if (selected2) selected2 = false; selected1 = true },
                colors = RadioButtonDefaults.colors(
                    selectedColor = MalltiverseTheme.colorScheme.primary,
                    unselectedColor = Color.Gray
                ),
            )

            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = "Shoto Street Area 1, Gark Area 1 AMAC",
                textDecoration = TextDecoration.Underline,
                style = MalltiverseTheme.typography.body,
                color = Color.Gray
            )
        }

        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .fillMaxWidth()
        ) {
            RadioButton(
                modifier = Modifier
                    .size(24.dp),
                selected = selected2,
                onClick = { if (selected1) selected1 = false; selected2 = true },
                colors = RadioButtonDefaults.colors(
                    selectedColor = MalltiverseTheme.colorScheme.primary,
                    unselectedColor = Color.Gray
                ),
            )

            Text(
                modifier = Modifier.padding(start = 4.dp, top = 4.dp, bottom = 4.dp, end = 8.dp),
                text = "Old Secretariat Complex, Area 1 Abaji",
                textDecoration = TextDecoration.Underline,
                style = MalltiverseTheme.typography.body,
                color = Color.Gray
            )
        }

        Text(
            text = stringResource(R.string.delivery),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, bottom = 4.dp, end = 16.dp),
            style = MalltiverseTheme.typography.body,
            fontWeight = FontWeight.SemiBold
        )

        OutlinedTextField(
            value = delivery,
            onValueChange = {
                delivery = it
                deliveryError = validateDelivery(delivery)
            },
            shape = shape.button,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MalltiverseTheme.colorScheme.background,
                unfocusedContainerColor = MalltiverseTheme.colorScheme.background,
                focusedIndicatorColor = MalltiverseTheme.colorScheme.primary,
                cursorColor = MalltiverseTheme.colorScheme.primary,
            ),
            isError = deliveryError != null,
            supportingText = {
                if (deliveryError != null)
                    Text(
                        deliveryError ?: "",
                        color = Color.Red,
                        style = MalltiverseTheme.typography.labelSmall,
                        modifier = Modifier.padding(4.dp)
                    )
            }
        )

        Text(
            text = stringResource(R.string.contact),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, bottom = 4.dp, end = 16.dp),
            style = MalltiverseTheme.typography.body,
            fontWeight = FontWeight.SemiBold
        )

        OutlinedTextField(
            value = contact1,
            onValueChange = {
                contact1 = it
                contact1Error = validateContact(contact1)
            },
            shape = shape.button,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
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
                    text = stringResource(R.string.phone_nos_1),
                    style = MalltiverseTheme.typography.body,
                    color = Color.LightGray
                )
            },
            isError = contact1Error != null,
            supportingText = {
                if (contact1Error != null)
                    Text(
                        contact1Error ?: "",
                        color = Color.Red,
                        style = MalltiverseTheme.typography.labelSmall,
                        modifier = Modifier.padding(4.dp)
                    )
            }
        )

        OutlinedTextField(
            value = contact2,
            onValueChange = {
                contact2 = it
                contact2Error = validateContact(contact2)
            },
            shape = shape.button,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .width(180.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MalltiverseTheme.colorScheme.background,
                unfocusedContainerColor = MalltiverseTheme.colorScheme.background,
                focusedIndicatorColor = MalltiverseTheme.colorScheme.primary,
                cursorColor = MalltiverseTheme.colorScheme.primary,
                focusedTextColor = MalltiverseTheme.colorScheme.onContainer,
                unfocusedTextColor = MalltiverseTheme.colorScheme.onContainer
            ),
            placeholder = {
                Text(
                    text = stringResource(R.string.phone_nos_2),
                    style = MalltiverseTheme.typography.body,
                    color = Color.LightGray,
                    modifier = Modifier.background(Color.Transparent)
                )
            },
            isError = contact2Error != null,
            supportingText = {
                if (contact2Error != null)
                    Text(
                        contact2Error ?: "",
                        color = Color.Red,
                        style = MalltiverseTheme.typography.labelSmall,
                        modifier = Modifier.padding(4.dp)
                    )
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(50.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            onClick = onGoToPaymentClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MalltiverseTheme.colorScheme.primary,
                contentColor = MalltiverseTheme.colorScheme.onPrimary
            ),
            shape = shape.button,
            enabled = deliveryError == null && contact1Error == null && contact2Error == null &&
                    delivery.isNotEmpty() && contact1.isNotEmpty()
        ) {
            Text(
                text = stringResource(R.string.go_to_payment)
            )
        }
    }
}

private fun validateDelivery(input: String): String? {
    return if (input.isEmpty()) "Delivery address is required" else null
}

private fun validateContact(input: String): String? {
    return if (input.isEmpty()) "At least 1 contact is required" else null
}


@Preview(showBackground = true)
@Composable
fun CheckoutScreenPreview() {
    CheckoutScreen(
        onGoToPaymentClick = {}
    )
}