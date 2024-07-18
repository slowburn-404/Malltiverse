package dev.borisochieng.malltiverse.presentation.ui.nav

sealed class PaymentFlow (
    override val route: String,
    override val title: String,
    val paymentScreen: PaymentFlowNavItems.Payment,
    val paymentSuccessfulScreen: PaymentFlowNavItems.PaymentSuccess
): NavItem {
    data object PaymentFlowScreens : PaymentFlow(
        route = "payment_flow_screens",
        title = "Payment",
        paymentScreen = PaymentFlowNavItems.Payment,
        paymentSuccessfulScreen = PaymentFlowNavItems.PaymentSuccess
    )
}
