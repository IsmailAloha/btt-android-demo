package com.bluetriangle.bluetriangledemo.compose.screens

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asFlow
import com.bluetriangle.analytics.compose.BttTimerEffect
import com.bluetriangle.bluetriangledemo.compose.ManualTimerEffect
import com.bluetriangle.bluetriangledemo.compose.components.ErrorAlertDialog
import com.bluetriangle.bluetriangledemo.compose.theme.outline
import com.bluetriangle.bluetriangledemo.data.Product
import com.bluetriangle.bluetriangledemo.ui.products.ProductsViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@Composable
fun ProductsScreen(productsViewModel: ProductsViewModel = hiltViewModel()) {
    BttTimerEffect(screenName = "Product Tab")
    ManualTimerEffect(screenName = "ProductsTabManualTimer")
    val products = productsViewModel.products.asFlow().collectAsState(listOf())
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(products.value.size) {
            ProductItem(products.value[it])
        }
    }
    ErrorAlertDialog(errorHandler = productsViewModel.errorHandler)
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun ProductItem(item: Product) {
    val context = LocalContext.current
    Card(
        elevation = 0.dp,
        border = BorderStroke(1.dp, MaterialTheme.colors.outline),
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        onClick = {
            context.startActivity(Intent(context, ProductDetailsActivity::class.java).apply {
                putExtra("product", item)
            })
        },
        content = {
            Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                GlideImage(
                    model = item.image, contentDescription = item.description, modifier = Modifier
                        .height(80.dp)
                        .width(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = String.format("%d : %s", item.id, item.name),
                        style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = String.format("$%.2f", item.price),
                        style = TextStyle(fontSize = 14.sp)
                    )
                }
            }
        })
}