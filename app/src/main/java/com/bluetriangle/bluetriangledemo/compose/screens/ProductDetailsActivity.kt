package com.bluetriangle.bluetriangledemo.compose.screens

import android.os.Bundle
import android.util.Log
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.IntentCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.bluetriangle.analytics.Tracker
import com.bluetriangle.bluetriangledemo.DemoApplication
import com.bluetriangle.bluetriangledemo.R
import com.bluetriangle.bluetriangledemo.compose.components.ErrorAlertDialog
import com.bluetriangle.bluetriangledemo.compose.components.MemoryWarningDialog
import com.bluetriangle.bluetriangledemo.compose.theme.BlueTriangleComposeDemoTheme
import com.bluetriangle.bluetriangledemo.compose.theme.outline
import com.bluetriangle.bluetriangledemo.data.Product
import com.bluetriangle.bluetriangledemo.tests.MemoryMonitor
import com.bluetriangle.bluetriangledemo.ui.products.ProductDetailViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bluetriangle.bluetriangledemo.utils.MemoryHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ProductDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val product = IntentCompat.getParcelableExtra(intent, "product", Product::class.java)

        if(Tracker.instance == null) {
            (application as? DemoApplication)?.initTracker()
        }
        setContent {
            val memoryWarningDialog = rememberSaveable {
                mutableStateOf<MemoryMonitor.MemoryWarning?>(null)
            }
            (application as DemoApplication).memoryMonitor.memoryWarningListener =
                object : MemoryMonitor.MemoryWarningListener {
                    override fun onMemoryWarning(memoryWarning: MemoryMonitor.MemoryWarning) {
                        Log.d("MemoryMonitorLogTag", "memory warning received")
                        runOnUiThread {
                            memoryWarningDialog.value = memoryWarning
                        }
                    }
                }
            BlueTriangleComposeDemoTheme {
                Scaffold(topBar = { TopAppBar(title = { Text(text = "Product Details") }) }) {
                    ProductDetails(it, product!!)
                }
                memoryWarningDialog.value?.let {
                    MemoryWarningDialog {
                        memoryWarningDialog.value = null
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
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
        val addToCartDescription = LocalContext.current.getString(R.string.a11y_add_to_cart)
        val addToCartCount = rememberSaveable {
            mutableIntStateOf(0)
        }
        Box(modifier = Modifier.padding(paddingValues)) {
            Card(
                elevation = 0.dp,
                border = BorderStroke(1.dp, MaterialTheme.colors.outline),
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                content = {
                    Column(Modifier.padding(8.dp)) {
                        GlideImage(
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
                                    addToCartCount.intValue += 1
                                    viewModel.handleProductDetailsTestCases(product, addToCartCount.intValue)
                                    scope.launch {
                                        try {
                                            addingToCart.value = true
                                            viewModel.cartRepository.addToCart(product)
                                        } catch (e: Exception) {
                                            viewModel.errorHandler.showError(e)
                                            withContext(Dispatchers.Main) {
                                                addingToCart.value = false
                                            }
                                        }
                                        withContext(Dispatchers.Main) {
                                            addingToCart.value = false
                                        }
                                    }
                                },
                                modifier = Modifier.semantics { contentDescription = addToCartDescription }) {
                                Icon(Icons.Filled.Add, "Add to Cart")
                                Spacer(modifier = Modifier.padding(10.dp))
                                Text("Add to Cart")
                            }
                        }
                    }
                })
        }
        ErrorAlertDialog(errorHandler = viewModel.errorHandler)
    }

    override fun onStart() {
        super.onStart()
        DemoApplication.checkAndAddDelay()
    }

    override fun onResume() {
        super.onResume()
        DemoApplication.checkAndAddDelay()
    }

    override fun onPause() {
        super.onPause()
        MemoryHolder.clearMemory()
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