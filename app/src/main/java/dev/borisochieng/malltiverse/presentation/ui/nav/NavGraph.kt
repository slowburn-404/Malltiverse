package dev.borisochieng.malltiverse.presentation.ui.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import dev.borisochieng.malltiverse.presentation.MainActivityViewModel
import dev.borisochieng.malltiverse.presentation.ui.screens.CartScreen
import dev.borisochieng.malltiverse.presentation.ui.screens.CheckoutScreen
import dev.borisochieng.malltiverse.presentation.ui.screens.HomeScreen
import dev.borisochieng.malltiverse.presentation.ui.screens.OrderHistoryScreen
import dev.borisochieng.malltiverse.presentation.ui.screens.PaymentScreen
import dev.borisochieng.malltiverse.presentation.ui.screens.PaymentSuccessfulScreen


@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: MainActivityViewModel,
    snackBarHostState: SnackbarHostState
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItems.Home.route,
    ) {
        composable(route = BottomNavItems.Home.route) {
            HomeScreen(viewModel = viewModel, snackBarHostState = snackBarHostState)
        }
        composable(route = BottomNavItems.Cart.route) {
            CartScreen(viewModel = viewModel, onCheckoutClick = {
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
        composable(route = PaymentFlowNavItems.OrderHistory.route) {
            OrderHistoryScreen()
        }
    }
}