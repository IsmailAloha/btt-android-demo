package com.bluetriangle.bluetriangledemo.compose.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bluetriangle.bluetriangledemo.DemoApplication
import com.bluetriangle.bluetriangledemo.compose.theme.BlueTriangleComposeDemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BlueTriangleComposeDemoTheme {
                Scaffold(topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                "Site Configuration", color = Color.White
                            )
                        },
                        backgroundColor = MaterialTheme.colors.primary
                    )
                }) {
                    SiteConfigurationForm(it)
                }
            }
        }
    }

}

@Composable
fun SiteConfigurationForm(paddingValues: PaddingValues) {
    val siteId = rememberSaveable {
        mutableStateOf("")
    }
    val isError = rememberSaveable {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(20.dp)
        ) {
            OutlinedTextField(isError = isError.value,
                value = siteId.value,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Site ID") },
                onValueChange = {
                    siteId.value = it
                },
                trailingIcon = {
                    if (isError.value)
                        Icon(Icons.Filled.Warning, "error", tint = MaterialTheme.colors.error)
                }
            )
            if (isError.value) {
                Text(
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp),
                    text = "Please Enter Site ID",
                    style = TextStyle(
                        fontSize = 14.sp
                    ),
                    color = MaterialTheme.colors.error
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {
                isError.value = siteId.value.isEmpty()
                if(siteId.value.isNotEmpty()) {
                    (context.applicationContext as DemoApplication).intTracker(siteId.value)
                    context.startActivity(Intent(context, ComposeStoreActivity::class.java))
                }
            }) {
                Text(text = "Save")
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!", modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BlueTriangleComposeDemoTheme {
        Greeting("Android")
    }
}