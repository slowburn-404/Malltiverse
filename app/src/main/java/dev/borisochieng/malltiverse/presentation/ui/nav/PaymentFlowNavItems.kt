package dev.borisochieng.malltiverse.presentation.ui.nav

sealed class PaymentFlowNavItems(
    override val route: String,
    override val title: String
): NavItem {
    data object Payment : PaymentFlowNavItems(
        route = "payment_screen",
        title = "Payment"
    )
    data object PaymentSuccess : PaymentFlowNavItems(
        route = "payment_complete",
        title = "Payment"
    )
}