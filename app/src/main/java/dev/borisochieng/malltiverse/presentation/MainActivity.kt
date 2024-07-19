package dev.borisochieng.malltiverse.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.borisochieng.malltiverse.MalltiverseApplication
import dev.borisochieng.malltiverse.R
import dev.borisochieng.malltiverse.presentation.ui.nav.NavGraph
import dev.borisochieng.malltiverse.presentation.ui.nav.BottomNavItems
import dev.borisochieng.malltiverse.presentation.ui.components.ScreenTitle
import dev.borisochieng.malltiverse.presentation.ui.components.getIcons
import dev.borisochieng.malltiverse.presentation.ui.theme.MalltiverseTheme
import dev.borisochieng.malltiverse.util.Constants.API_KEY
import dev.borisochieng.malltiverse.util.Constants.APP_ID
import dev.borisochieng.malltiverse.util.Constants.ORGANIZATION_ID
import dev.borisochieng.malltiverse.util.UIEvents
import dev.borisochieng.malltiverse.presentation.ui.nav.PaymentFlowNavItems

class MainActivity : ComponentActivity() {
    private val mainActivityViewModel: MainActivityViewModel by viewModels {
        val application = application as MalltiverseApplication
        MainActivityViewModelFactory(
            application.timbuAPIRepositoryImpl,
            application.dispatcherProvider
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by mainActivityViewModel.uiState.collectAsState()
            val snackBarHostState = remember { SnackbarHostState() }
            val navController = rememberNavController()
            val bottomNavItems = listOf(
                BottomNavItems.Home,
                BottomNavItems.Cart,
                BottomNavItems.Checkout
            )
            val paymentFlowNavItems = listOf(
                PaymentFlowNavItems.Payment,
                PaymentFlowNavItems.PaymentComplete,
                PaymentFlowNavItems.OrderHistory
            )

            val navCurrentBackStackEntry by navController.currentBackStackEntryAsState()
            val currentScreen =
                bottomNavItems.find { it.route == navCurrentBackStackEntry?.destination?.route }
                    ?: paymentFlowNavItems.find { it.route == navCurrentBackStackEntry?.destination?.route }

            var selectedItemIndex by remember {
                mutableStateOf(0)
            }

            LaunchedEffect(navCurrentBackStackEntry) {
                val newSelectedIndex = bottomNavItems.indexOfFirst {
                    it.route == currentScreen?.route
                }
                if (newSelectedIndex >= 0) {
                    selectedItemIndex = newSelectedIndex
                }
            }

            LaunchedEffect(Unit) {
                mainActivityViewModel.eventFlow.collect { event ->
                    if (event is UIEvents.SnackBarEvent) {
                        snackBarHostState.showSnackbar(event.message)
                    }
                }
            }

            MalltiverseTheme {
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackBarHostState,
                            snackbar = {
                                Snackbar(
                                    modifier = Modifier
                                        .padding(16.dp),
                                    action = {
                                        Text(
                                            text = "RETRY",
                                            style = MalltiverseTheme.typography.body,
                                            color = MalltiverseTheme.colorScheme.primary,
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .clickable {
                                                    mainActivityViewModel.getProducts(
                                                        apiKey = API_KEY,
                                                        organizationID = ORGANIZATION_ID,
                                                        appId = APP_ID
                                                    )
                                                },
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                ) {
                                    Text(text = uiState.errorMessage)
                                }
                            }
                        )
                    },
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.systemBars),
                    topBar = {
                        TopAppBar(
                            title = {
                                ScreenTitle(
                                    title = currentScreen?.title ?: "Malltiverse",
                                )

                            },
                            modifier = Modifier
                                .windowInsetsPadding(WindowInsets.systemBars),
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MalltiverseTheme.colorScheme.background
                            ),
                            actions = {
                                IconButton(
                                    onClick = {
                                        navController.navigate(PaymentFlowNavItems.OrderHistory.route)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.DateRange,
                                        contentDescription = stringResource(R.string.order_history)
                                    )
                                }
                            }
                        )
                    },
                    bottomBar = {
                        val icons = getIcons()
                        Box(
                            modifier = Modifier
                                .height(90.dp)
                                .padding(16.dp)
                                .background(
                                    color = MalltiverseTheme.colorScheme.onBackground,
                                    shape = MalltiverseTheme.shape.bottomNav
                                )
                                .shadow(elevation = 4.dp, shape = MalltiverseTheme.shape.bottomNav),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .height(90.dp),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                bottomNavItems.forEachIndexed { index, bottomNavScreen ->
                                    NavigationBarItem(
                                        selected = selectedItemIndex == index,
                                        onClick = {
                                            if (selectedItemIndex != index) {
                                                navController.navigate(bottomNavScreen.route) {
                                                    popUpTo(navController.graph.startDestinationId) {
                                                        inclusive = false
                                                    }
                                                    launchSingleTop = true
                                                }
                                            }
                                        },
                                        icon = {
                                            val icon = icons[bottomNavScreen.route]

                                            Box(
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .background(
                                                        color =
                                                        if (selectedItemIndex == index) {
                                                            MalltiverseTheme.colorScheme.primary
                                                        } else {
                                                            Color.Transparent
                                                        },
                                                        shape = CircleShape
                                                    )
                                                    .padding(8.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                if (icon != null) {
                                                    Icon(
                                                        imageVector = icon,
                                                        contentDescription = bottomNavScreen.title,
                                                        tint = if (selectedItemIndex == index) Color.Black else Color.White,
                                                    )
                                                }

                                            }

                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = Color.Transparent,
                                            unselectedIconColor = Color.Transparent,
                                            indicatorColor = Color.Transparent
                                        )

                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = MalltiverseTheme.colorScheme.background
                    ) {
                        NavGraph(
                            navController = navController,
                            viewModel = mainActivityViewModel,
                            snackBarHostState = snackBarHostState
                        )
                    }
                }
            }
        }
    }
}