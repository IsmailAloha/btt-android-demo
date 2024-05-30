package com.bluetriangle.bluetriangledemo.compose.screens

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bluetriangle.analytics.Tracker
import com.bluetriangle.bluetriangledemo.DemoApplication
import com.bluetriangle.bluetriangledemo.R
import com.bluetriangle.bluetriangledemo.compose.components.AppBottomNavigationBar
import com.bluetriangle.bluetriangledemo.compose.components.NavHostContainer
import com.bluetriangle.bluetriangledemo.compose.components.NavItem
import com.bluetriangle.bluetriangledemo.compose.theme.BlueTriangleComposeDemoTheme
import com.bluetriangle.bluetriangledemo.utils.copyToClipboard
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class ComposeStoreActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BlueTriangleComposeDemoTheme {
                val title = rememberSaveable {
                    mutableStateOf("")
                }
                val navController = rememberNavController()
                val navItems = getNavItemsList(navController)
                Scaffold(topBar = {
                    TopAppBar(title = { Text(text = title.value) })
                }, bottomBar = {
                    AppBottomNavigationBar(navController = navController, navItems = navItems)
                }) {
                    Column(
                        modifier = Modifier
                            .padding(it)
                            .fillMaxHeight()
                    ) {
                        SiteIDBar()
                        NavHostContainer(
                            title,
                            navController = navController,
                            navItems = navItems
                        )
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
    fun SiteIDBar() {
        val sessionIdAccessibility = stringResource(id = R.string.sessionid_accessibility)
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
                    text = Tracker.instance?.configuration?.sessionId ?: "",
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier
                        .semantics {
                            contentDescription = sessionIdAccessibility
                        }
                        .clickable {
                            context.copyToClipboard("Session ID", Tracker.instance?.configuration?.sessionId ?: "")
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
                NavItem.Destination("Product", "product/home") { ProductsScreen() }
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
                NavItem.Destination("Cart", "cart/home") { CartScreen({
                    navController.navigate("cart/checkout/${UUID.randomUUID()}")
                }) },
                NavItem.Destination(
                    "Checkout",
                    "cart/checkout/{checkoutId}"
                ) { CheckoutScreen(it.arguments?.getString("checkoutId") ?: "") }
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

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    BlueTriangleComposeDemoTheme {
        Greeting2("Android")
    }
}