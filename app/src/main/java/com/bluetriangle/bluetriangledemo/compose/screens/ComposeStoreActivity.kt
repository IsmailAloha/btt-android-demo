package com.bluetriangle.bluetriangledemo.compose.screens

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bluetriangle.bluetriangledemo.ConfigurationManager
import com.bluetriangle.bluetriangledemo.DemoApplication
import com.bluetriangle.bluetriangledemo.R
import com.bluetriangle.bluetriangledemo.compose.components.AppBottomNavigationBar
import com.bluetriangle.bluetriangledemo.compose.components.NavHostContainer
import com.bluetriangle.bluetriangledemo.compose.components.NavItem
import com.bluetriangle.bluetriangledemo.compose.theme.BlueTriangleComposeDemoTheme
import com.bluetriangle.bluetriangledemo.utils.copyToClipboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComposeStoreActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BlueTriangleComposeDemoTheme {
                val title = rememberSaveable {
                    mutableStateOf("")
                }
                val tabNavController = rememberNavController()
                val rootNavController = rememberNavController()
                val rootBackstackEntry by rootNavController.currentBackStackEntryAsState()

                val navItems = getNavItemsList(tabNavController)
                Scaffold(topBar = {
                    TopAppBar(title = {
                        Row(Modifier.padding(end = 10.dp)) {
                            Text(text = title.value)
                            Spacer(modifier = Modifier.weight(1f))
                            if(title.value != "Profile") {
                                Text(
                                    text = "Profile",
                                    fontSize = 14.sp,
                                    modifier = Modifier.clickable { rootNavController.navigate("profile") }
                                )
                            }
                        }
                    })
                }, bottomBar = {
                    if(rootBackstackEntry?.destination?.route == "home") {
                        AppBottomNavigationBar(navController = tabNavController, navItems = navItems)
                    }
                }) { paddingValues ->
                    NavHost(navController = rootNavController, startDestination = "home") {
                        composable("home") {
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(paddingValues)
                            ) {
                                SessionIDBar()
                                NavHostContainer(
                                    title,
                                    navController = tabNavController,
                                    navItems = navItems
                                )
                            }
                        }
                        composable("profile") {
                            title.value = "Profile"
                            ProfileScreen()
                        }
                    }
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        DemoApplication.checkAndAddDelay()
    }

    override fun onResume() {
        super.onResume()
        DemoApplication.checkAndAddDelay()
    }

    @Composable
    fun SessionIDBar() {
        val sessionIdAccessibility = stringResource(id = R.string.a11y_btt_session_id)
        val sessionId by ConfigurationManager.sessionId.observeAsState()
        val context = LocalContext.current
        Column(modifier = Modifier.background(MaterialTheme.colors.primary)) {
            Row(
                modifier = Modifier
                    .padding(16.dp, 8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Session ID: ",
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = sessionId ?: "",
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier
                        .semantics {
                            contentDescription = sessionIdAccessibility
                        }
                        .clickable {
                            context.copyToClipboard(
                                "Session ID",
                                sessionId ?: ""
                            )
                        }
                )
            }
        }
    }
}

fun getNavItemsList(navController: NavHostController): List<NavItem> {
    return listOf(
        NavItem(
            "Products",
            icon = {
                Icon(
                    painterResource(id = R.drawable.baseline_apps_24),
                    contentDescription = "Products",
                    tint = it
                )
            },
            "product",
            destinations = listOf(
                NavItem.Destination("Product", "product/home") { ProductsScreen() },
            )
        ),
        NavItem("Cart", icon = {
            Icon(
                Icons.Outlined.ShoppingCart,
                contentDescription = "Cart",
                tint = it
            )
        }, "cart",
            destinations = listOf(
                NavItem.Destination("Cart", "cart/home") { CartScreen({ uuid ->
                    navController.navigate("cart/checkout/${uuid}")
                }) },
                NavItem.Destination(
                    "Checkout",
                    "cart/checkout/{checkoutId}"
                ) { CheckoutScreen(it.arguments?.getString("checkoutId") ?: "", navController) }
            )),
        NavItem("Settings", icon = {
            Icon(
                Icons.Filled.Settings,
                contentDescription = "Settings",
                tint = it
            )
        }, "settings",
            destinations = listOf(
                NavItem.Destination("Settings", "settings/home") { SettingsScreen() }
            ))
    )
}