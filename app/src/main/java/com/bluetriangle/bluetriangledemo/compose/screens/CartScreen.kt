package com.bluetriangle.bluetriangledemo.compose.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asFlow
import coil.compose.AsyncImage
import com.bluetriangle.analytics.compose.BttTimerEffect
import com.bluetriangle.bluetriangledemo.R
import com.bluetriangle.bluetriangledemo.data.Cart
import com.bluetriangle.bluetriangledemo.data.CartItem
import com.bluetriangle.bluetriangledemo.ui.cart.CartViewModel

@Composable
fun CartScreen(viewModel: CartViewModel = hiltViewModel()) {
    BttTimerEffect(screenName = "Cart Tab")
    val cart = viewModel.cart.asFlow().collectAsState(null)
    Column(
        Modifier
            .background(Color(0x22000000))
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        cart.value?.items?.map { cartItem ->
            CartListItem(viewModel, cartItem)
        }
    }
}

@Composable
fun CartListItem(viewModel: CartViewModel, cartItem: CartItem) {
    Card(
        Modifier.padding(12.dp),
        border = BorderStroke(1.dp, Color(0xFF999999)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row {
            AsyncImage(
                model = cartItem.productReference?.image,
                contentDescription = cartItem.productReference?.description
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = cartItem.productReference?.name ?: "")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = cartItem.productReference?.name ?: "")
            }
            CartActionButton(cartItem, viewModel)
        }
    }
}

@Composable
fun CartActionButton(cartItem: CartItem, viewModel: CartViewModel) {
    if (cartItem.quantity > 1) {
        Row(
            Modifier
                .border(
                    1.dp,
                    Color(0xFF999999),
                    RoundedCornerShape(4.dp)
                )
                .padding(4.dp)
        ) {
            Button(onClick = { }) {
                Icon(
                    painterResource(id = R.drawable.baseline_remove_24),
                    contentDescription = "Remove"
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = cartItem.quantity.toString())
            Spacer(modifier = Modifier.width(10.dp))
            Button(onClick = { }) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    } else {
        Button(onClick = { }) {
            Icon(
                painterResource(id = R.drawable.baseline_remove_24),
                contentDescription = "Remove from Cart"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Remove from Cart")
        }
    }
}
