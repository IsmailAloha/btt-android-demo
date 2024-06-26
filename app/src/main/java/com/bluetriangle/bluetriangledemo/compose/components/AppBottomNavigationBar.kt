package com.bluetriangle.bluetriangledemo.compose.components

import android.util.Log
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun AppBottomNavigationBar(
    navController: NavHostController,
    navItems: List<NavItem>
) {
    val lastSelected = rememberSaveable {
        mutableStateOf(navItems[0].route)
    }

    BottomNavigation(
        // set background color
        backgroundColor = MaterialTheme.colors.background
    ) {

        val navBackStackEntry by navController.currentBackStackEntryAsState()

        // observe current route to change the icon
        // color,label color when navigated
        val currentRoute = navBackStackEntry?.destination?.route


        // Bottom nav items we declared
        navItems.forEach { navItem ->
            BottomNavigationItem(
                selected = currentRoute?.contains(navItem.route) == true,
                onClick = {
                    navController.navigate(navItem.route) {

                        popUpTo(lastSelected.value) { inclusive = true }
                        lastSelected.value = navItem.route
                    }
                },
                icon = {
                    navItem.icon(
                        if (currentRoute?.contains(navItem.route) == true) {
                            MaterialTheme.colors.primary
                        } else {
                            Color(0xFF999999)
                        }
                    )
                },
                label = {
                    Text(
                        text = navItem.label, style = TextStyle(
                            fontSize = 12.sp,
                            color = if (currentRoute?.contains(navItem.route) == true) {
                                MaterialTheme.colors.primary
                            } else {
                                Color(0xFF999999)
                            }
                        )
                    )
                },
                alwaysShowLabel = true
            )
        }
    }
}