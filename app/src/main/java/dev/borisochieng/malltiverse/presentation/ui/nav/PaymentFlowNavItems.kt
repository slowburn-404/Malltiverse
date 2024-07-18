package dev.borisochieng.malltiverse.presentation.ui.nav

sealed class PaymentFlowNavItems(
    override val route: String,
    override val title: String
): NavItem {
    data object Payment : PaymentFlowNavItems(
        route = "payment_screen",
        title = "Payment"
    )
    data object PaymentComplete : PaymentFlowNavItems(
        route = "payment_complete",
        title = "Payment"
    )

    //TODO(Move somewhere else)
    data object OrderHistory : PaymentFlowNavItems(
        route = "order_history",
        title = "Order History"
    )
}