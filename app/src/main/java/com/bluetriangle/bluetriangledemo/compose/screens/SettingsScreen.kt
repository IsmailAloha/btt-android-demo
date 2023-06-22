package com.bluetriangle.bluetriangledemo.compose.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bluetriangle.analytics.compose.BttTimerEffect

@Composable
fun SettingsScreen() {
    BttTimerEffect(screenName = "Settings Tab")
    Box(modifier = Modifier.fillMaxSize().background(Color(0x226200EE))) {
        Text(text = "Settings Screen", style = TextStyle(
            color = MaterialTheme.colors.onBackground,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black
        ),
        modifier = Modifier.align(Alignment.Center),
        )
    }
}