package dev.borisochieng.malltiverse.presentation.ui.nav

sealed class OtherNavItems(
    override val route: String,
    override val title: String
): NavItem {
    data object Payment : OtherNavItems(
        route = "payment_screen",
        title = "Payment"
    )
    data object PaymentComplete : OtherNavItems(
        route = "payment_complete",
        title = "Payment"
    )

    data object OrderHistory : OtherNavItems(
        route = "orders",
        title = "Orders"
    )

    data object Product: OtherNavItems(
        route = "product/{productId}",
        title = "Product"
    ) {
        fun createRoute(productId: String) = "product/$productId"
    }

    data object Wishlist: OtherNavItems(
        route = "wishlist",
        title = "Wishlist"
    )

    data object SingleOrder: OtherNavItems(
        route = "order/{orderId}",
        title = "Order"
    ) {
        fun createRoute(orderId: String) = "order/$orderId"
    }
}