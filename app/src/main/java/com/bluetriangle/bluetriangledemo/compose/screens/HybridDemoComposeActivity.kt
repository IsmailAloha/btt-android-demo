package com.bluetriangle.bluetriangledemo.compose.screens

import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bluetriangle.bluetriangledemo.BTTWebViewClient
import com.bluetriangle.bluetriangledemo.DemoApplication.Companion.DEMO_WEBSITE_URL
import com.bluetriangle.bluetriangledemo.R
import com.bluetriangle.bluetriangledemo.TitleChromeClient
import com.bluetriangle.bluetriangledemo.compose.theme.BlueTriangleComposeDemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComposeAboutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var title by rememberSaveable {
                mutableStateOf("")
            }
            var url by rememberSaveable {
                mutableStateOf(DEMO_WEBSITE_URL)
            }
            BlueTriangleComposeDemoTheme {
                Scaffold(topBar = {
                    TopAppBar(title = { Text(text = title, maxLines = 1, overflow = TextOverflow.Ellipsis) }, navigationIcon = {
                        IconButton(onClick = {
                            onBackPressedDispatcher.onBackPressed()
                        }, modifier = Modifier.padding(8.dp)) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                "Back",
                                tint = MaterialTheme.colors.onPrimary
                            )
                        }
                    }, actions = {
                        IconButton(onClick = {
                             url = "https://trackerdemo.github.io/hybrid-demo-info/"
                        }, modifier = Modifier.padding(8.dp)) {
                            Icon(
                                painterResource(id = R.drawable.baseline_help_24),
                                "About",
                                tint = MaterialTheme.colors.onPrimary
                            )
                        }
                    })
                }) {
                    Column(
                        modifier = Modifier
                            .padding(it)
                            .fillMaxSize()
                    ) {
                        HybridWebView(url, { title = it ?: DEMO_WEBSITE_URL }) {
                            url = it ?: DEMO_WEBSITE_URL
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun HybridWebView(url: String, setTitle: (String?) -> Unit, onUrlChange: (String?) -> Unit) {

    var backEnabled by rememberSaveable {
        mutableStateOf(false)
    }
    var webView: WebView? = null
    AndroidView(factory = { context ->
        WebView(context).apply {
            webView = this
            webViewClient = BTTWebViewClient {
                onUrlChange(it)
                backEnabled = canGoBack()
            }
            webChromeClient = TitleChromeClient(setTitle)
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.allowFileAccess = true
            WebView.setWebContentsDebuggingEnabled(true)
        }
    }, update = {
        webView = it
        it.loadUrl(url)
    })
    BackHandler(backEnabled) {
        webView?.goBack()
    }
}