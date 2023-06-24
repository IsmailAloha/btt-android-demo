package com.bluetriangle.bluetriangledemo.compose.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asFlow
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.bluetriangle.analytics.compose.BttTimerEffect
import com.bluetriangle.bluetriangledemo.R
import com.bluetriangle.bluetriangledemo.data.CartItem
import com.bluetriangle.bluetriangledemo.ui.cart.CartViewModel
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID.randomUUID

@Composable
fun CartScreen(navController:NavHostController, viewModel:CartViewModel = hiltViewModel()) {
    BttTimerEffect(screenName = "Cart_Screen")
    val scope = rememberCoroutineScope()
    val cart = viewModel.cart.asFlow().collectAsState(null)
    val cartItems = cart.value?.items ?: listOf()
    if (cartItems.isNotEmpty()) {
        Column(
            Modifier
                .padding(8.dp)
                .fillMaxSize()
    ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 150.dp),
                Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cartItems.size) { item ->
                    CartListItem(viewModel = viewModel, cartItem = cartItems[item])
                }
            }
            Button(modifier = Modifier.fillMaxWidth(), onClick = {
                scope.launch {
                    val cartValue = cart.value
                    if(cartValue != null) {
                        viewModel.cartRepository.checkout(cartValue)
                        viewModel.refreshCart()
                        withContext(Main) {
                            navController.navigate("cart/checkout/${randomUUID()}")
                        }
                    }
                }
            }) {
                Text(text = "Checkout")
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = "No Items in Cart", modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun CartListItem(viewModel: CartViewModel, cartItem: CartItem) {
    Card(
        Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, Color(0xFFEFEFEF)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(Modifier.padding(8.dp)) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        1.dp,
                        Color(0xFFE1E1E1),
                        RoundedCornerShape(8.dp)
                    ),
                model = cartItem.productReference?.image,
                contentDescription = cartItem.productReference?.description,
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = cartItem.productReference?.name ?: "")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = String.format("$%.2f", cartItem.price))
            CartActionButton(Modifier.align(Alignment.CenterHorizontally), cartItem, viewModel)
        }
    }
}

@Composable
fun CartActionButton(modifier: Modifier, cartItem: CartItem, viewModel: CartViewModel) {
    val scope = rememberCoroutineScope()
    Row(modifier) {
        IconButton(onClick = {
            scope.launch {
                if(cartItem.quantity <= 1) {
                    viewModel.cartRepository.removeCartItem(cartItem)
                } else {
                    viewModel.cartRepository.reduceQuantity(cartItem)
                }
            }
            viewModel.refreshCart()
        }, modifier = Modifier.background(Color.White, shape = CircleShape)) {
            Icon(
                painterResource(id = R.drawable.baseline_remove_24),
                contentDescription = "Remove",
                tint = MaterialTheme.colors.primary
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = cartItem.quantity.toString(), modifier = Modifier.align(Alignment.CenterVertically))
        Spacer(modifier = Modifier.width(16.dp))
        IconButton(onClick = {
            scope.launch {
                viewModel.cartRepository.increaseQuantity(cartItem)
            }
            viewModel.refreshCart()
        }, modifier = Modifier.background(Color.White, shape = CircleShape)) {
            Icon(Icons.Filled.Add, contentDescription = "Add", tint = MaterialTheme.colors.primary)
        }
    }
}
