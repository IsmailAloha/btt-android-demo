package com.bluetriangle.bluetriangledemo.compose.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavBackStackEntry

data class NavItem(
    val label: String,
    val icon: @Composable (Color)->Unit,
    val route: String,
    val destinations:List<Destination>
) {
    data class Destination(
        val label: String,
        val route: String,
        val screen: @Composable (NavBackStackEntry)->Unit
    )
}