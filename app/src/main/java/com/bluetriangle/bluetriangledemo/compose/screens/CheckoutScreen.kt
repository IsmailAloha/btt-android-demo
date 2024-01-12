package com.bluetriangle.bluetriangledemo.compose.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.bluetriangle.analytics.compose.BttTimerEffect
import com.bluetriangle.bluetriangledemo.tests.HeavyLoopTest

@Composable
fun CheckoutScreen(checkoutId: String) {
    BttTimerEffect(screenName = "Checkout_Screen")
    val navController = rememberNavController()

    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            Text(
                text = "Thanks for Ordering...", style = TextStyle(
                    color = MaterialTheme.colors.primary,
                    fontSize = 18.sp
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Checkout ID: $checkoutId")
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    HeavyLoopTest().run()
                    navController.popBackStack()
                }) {
                Text("Continue Shopping")
            }
        }
    }
}