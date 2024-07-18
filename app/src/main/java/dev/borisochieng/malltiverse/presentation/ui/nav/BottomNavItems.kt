package dev.borisochieng.malltiverse.presentation.ui.nav

sealed class BottomNavItems(
    override val route: String,
    override val title: String
): NavItem {
    data object Home : BottomNavItems(
        route = "home",
        title = "Product List"
    )

    data object Cart : BottomNavItems(
        route = "cart",
        title = "My Cart"
    )

    data object Checkout : BottomNavItems(
        route = "checkout",
        title = "Checkout"
    )

}