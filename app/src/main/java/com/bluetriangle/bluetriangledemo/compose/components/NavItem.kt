package com.bluetriangle.bluetriangledemo.compose.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class NavItem(
    val label: String,
    val icon: @Composable (Color)->Unit,
    val route: String,
    val screen: @Composable ()->Unit
)