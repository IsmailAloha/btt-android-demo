package com.bluetriangle.bluetriangledemo.compose.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation

@Composable
fun NavHostContainer(
    title: MutableState<String>,
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
                navigation(navItem.destinations[0].route, navItem.route) {
                    navItem.destinations.map { destination ->
                        composable(destination.route, content = {
                            title.value = destination.label
                            destination.screen(it)
                        })
                    }
                }
            }
        })

}