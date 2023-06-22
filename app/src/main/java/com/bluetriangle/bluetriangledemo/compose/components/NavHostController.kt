package com.bluetriangle.bluetriangledemo.compose.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavHostContainer(
    navController: NavHostController,
    padding: PaddingValues,
    navItems: List<NavItem>
) {

    NavHost(
        navController = navController,

        // set the start destination as home
        startDestination = navItems[0].route,

        // Set the padding provided by scaffold
        modifier = Modifier.padding(paddingValues = padding),

        builder = {
            navItems.map { navItem ->
                composable(navItem.route) {
                    navItem.screen()
                }
            }
        })

}