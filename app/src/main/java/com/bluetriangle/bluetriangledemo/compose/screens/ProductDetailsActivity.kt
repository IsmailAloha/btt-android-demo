package com.bluetriangle.bluetriangledemo.compose.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.IntentCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.bluetriangle.bluetriangledemo.compose.theme.BlueTriangleComposeDemoTheme
import com.bluetriangle.bluetriangledemo.data.Product
import com.bluetriangle.bluetriangledemo.ui.products.ProductDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ProductDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val product = IntentCompat.getParcelableExtra(intent, "product", Product::class.java)

        setContent {
            BlueTriangleComposeDemoTheme {
                Scaffold(topBar = { TopAppBar(title = { Text(text = "Product Details") }) }) {
                    ProductDetails(it, product!!)
                }
            }
        }
    }

    @Composable
    private fun ProductDetails(
        paddingValues: PaddingValues,
        product: Product,
        viewModel: ProductDetailViewModel = hiltViewModel()
    ) {
        val scope = rememberCoroutineScope()
        val addingToCart = rememberSaveable {
            mutableStateOf(false)
        }
        Box(modifier = Modifier.padding(paddingValues)) {
            Card(
                elevation = 0.dp,
                border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                content = {
                    Column(Modifier.padding(8.dp)) {
                        AsyncImage(
                            model = product.image,
                            contentDescription = product.description,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Column(
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = product.name,
                                style = TextStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp
                                )
                            )
                            Spacer(modifier = Modifier.padding(2.dp))
                            Text(
                                text = String.format("$%.2f", product.price),
                                style = TextStyle(fontSize = 14.sp)
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                            Text(
                                text = product.description,
                                style = TextStyle(fontSize = 14.sp)
                            )
                            Spacer(modifier = Modifier.padding(20.dp))
                            Button(
                                enabled = !addingToCart.value,
                                onClick = {
                                    scope.launch {
                                        try {
                                            addingToCart.value = true
                                            viewModel.cartRepository.addToCart(product)
                                        } catch (e: Exception) {
                                            withContext(Dispatchers.Main) {
                                                addingToCart.value = false
                                            }
                                        }
                                        withContext(Dispatchers.Main) {
                                            addingToCart.value = false
                                        }
                                    }
                                }) {
                                Icon(Icons.Filled.Add, "Add to Cart")
                                Spacer(modifier = Modifier.padding(10.dp))
                                Text("Add to Cart")
                            }
                        }
                    }
                })
        }
    }
}

@Composable
fun Greeting3(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    BlueTriangleComposeDemoTheme {
        Greeting3("Android")
    }
}