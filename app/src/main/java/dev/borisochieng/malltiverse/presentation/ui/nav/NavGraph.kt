package dev.borisochieng.malltiverse.presentation.ui.nav

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import dev.borisochieng.malltiverse.presentation.MainActivityViewModel
import dev.borisochieng.malltiverse.presentation.ui.screens.cart.CartScreen
import dev.borisochieng.malltiverse.presentation.ui.screens.CheckoutScreen
import dev.borisochieng.malltiverse.presentation.ui.screens.HomeScreen
import dev.borisochieng.malltiverse.presentation.ui.screens.orderhistory.OrderHistoryScreen
import dev.borisochieng.malltiverse.presentation.ui.screens.PaymentScreen
import dev.borisochieng.malltiverse.presentation.ui.screens.PaymentSuccessfulScreen
import dev.borisochieng.malltiverse.presentation.ui.screens.ProductScreen
import dev.borisochieng.malltiverse.presentation.ui.screens.SingleOrderScreen
import dev.borisochieng.malltiverse.presentation.ui.screens.WishlistScreen
import dev.borisochieng.malltiverse.presentation.ui.screens.orderhistory.OrderHistoryViewModel


@Composable
fun NavGraph(
    navController: NavHostController,
    mainActivityViewModel: MainActivityViewModel,
    snackBarHostState: SnackbarHostState,
    orderHistoryViewModel: OrderHistoryViewModel
) {

    NavHost(
        navController = navController,
        startDestination = BottomNavItems.Home.route,
    ) {
        composable(route = BottomNavItems.Home.route) {
            HomeScreen(
                mainActivityViewModel = mainActivityViewModel,
                snackBarHostState = snackBarHostState
            )
        }
        composable(route = BottomNavItems.Cart.route) {
            CartScreen(viewModel = mainActivityViewModel, onCheckoutClick = {
                navController.navigate(BottomNavItems.Checkout.route)
            })
        }
        composable(route = BottomNavItems.Checkout.route) {
            CheckoutScreen(
                onGoToPaymentClick = {
                    navController.navigate(PaymentFlow.PaymentFlowScreens.route)
                }
            )
        }

        navigation(
            startDestination = PaymentFlow.PaymentFlowScreens.paymentScreen.route,
            route = PaymentFlow.PaymentFlowScreens.route
        ) {
            composable(route = PaymentFlow.PaymentFlowScreens.paymentScreen.route) {
                PaymentScreen(onMakePaymentClick = {
                    mainActivityViewModel.addCartItemsToLocalDb()
                    mainActivityViewModel.clearCart()
                    navController.navigate(PaymentFlow.PaymentFlowScreens.paymentSuccessfulScreen.route)
                })
            }
            composable(route = PaymentFlow.PaymentFlowScreens.paymentSuccessfulScreen.route) {
                PaymentSuccessfulScreen(onPaymentComplete = {
                    navController.navigate(BottomNavItems.Home.route) {
                        popUpTo(BottomNavItems.Home.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }

                })
            }
        }
        composable(route = OtherNavItems.OrderHistory.route) {
            OrderHistoryScreen(orderHistoryViewModel, snackBarHostState, onCardClick = { product ->
                navController.navigate(OtherNavItems.SingleOrder.createRoute(product.id))
            })
        }

        composable(
            route = OtherNavItems.Product.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")

            Log.d("Product ID", productId.toString())

            if (productId != null) {
                ProductScreen(
                    productID = productId,
                    viewModel = mainActivityViewModel,
                    snackBarHostState = snackBarHostState
                )
            }
        }

        composable(
            route = OtherNavItems.Wishlist.route
        ) {
            WishlistScreen(
                viewModel = mainActivityViewModel,
                snackBarHostState = snackBarHostState,
                onCardClick = { product ->
                    navController.navigate(OtherNavItems.Product.createRoute(product.id))
                })
        }

        composable(
            route = OtherNavItems.SingleOrder.route,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId")
            if (orderId != null) {
                SingleOrderScreen(orderId = orderId, viewModel = mainActivityViewModel)
            }
        }
    }
}