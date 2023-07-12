package com.bluetriangle.bluetriangledemo.compose.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bluetriangle.bluetriangledemo.R
import com.bluetriangle.bluetriangledemo.compose.components.AppBottomNavigationBar
import com.bluetriangle.bluetriangledemo.compose.components.NavHostContainer
import com.bluetriangle.bluetriangledemo.compose.components.NavItem
import com.bluetriangle.bluetriangledemo.compose.theme.BlueTriangleComposeDemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComposeStoreActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val title = rememberSaveable {
                mutableStateOf("")
            }
            BlueTriangleComposeDemoTheme {
                val navController = rememberNavController()
                val navItems = getNavItemsList(navController)
                Scaffold(topBar = {
                    TopAppBar(title = { Text(text = title.value) })
                }, bottomBar = {
                    AppBottomNavigationBar(navController = navController, navItems = navItems)
                }) {
                    NavHostContainer(
                        title,
                        navController = navController,
                        padding = it,
                        navItems = navItems
                    )
                }
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
                NavItem.Destination("Cart", "cart/home") { CartScreen(navController) },
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