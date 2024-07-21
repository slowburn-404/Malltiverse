package dev.borisochieng.malltiverse.presentation.ui.nav

sealed class PaymentFlow (
    override val route: String,
    override val title: String,
    val paymentScreen: OtherNavItems.Payment,
    val paymentSuccessfulScreen: OtherNavItems.PaymentComplete
): NavItem {
    data object PaymentFlowScreens : PaymentFlow(
        route = "payment_flow_screens",
        title = "Payment",
        paymentScreen = OtherNavItems.Payment,
        paymentSuccessfulScreen = OtherNavItems.PaymentComplete
    )
}
