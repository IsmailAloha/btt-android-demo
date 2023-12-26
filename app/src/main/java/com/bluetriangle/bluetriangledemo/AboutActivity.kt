package com.bluetriangle.bluetriangledemo

import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.bluetriangle.analytics.Tracker
import com.bluetriangle.bluetriangledemo.DemoApplication.Companion.DEFAULT_WEBSITE_URL
import com.bluetriangle.bluetriangledemo.databinding.ActivityAboutBinding
import java.security.AccessController.getContext


class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle(R.string.about_us)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        binding.webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return loadUrl(view, request?.url?.toString())
            }

            private fun loadUrl(view: WebView?, url: String?): Boolean {
                if (url != null) {
                    view?.loadUrl(url)
                    return true
                }
                return false
            }

            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return loadUrl(view, url)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Tracker.instance?.configuration?.sessionId?.let {
                    val expiration = (System.currentTimeMillis() + 1800000).toString()
                    val setSession = "{\"value\":\"$it\",\"expires\":\"$expiration\"}"
                    binding.webView.evaluateJavascript(
                        "window.localStorage.setItem('BTT_X0siD', '$setSession');"
                    ) {
                        Tracker.instance?.configuration?.logger?.info("Injected SessionID in WebView: $setSession, $it")
                    }
                    binding.webView.evaluateJavascript(
                        "window.localStorage"
                    ) {
                        Tracker.instance?.configuration?.logger?.info(it)
                    }
                }
            }
        }
        binding.webView.settings.javaScriptEnabled = true
        WebView.setWebContentsDebuggingEnabled(true)
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.databaseEnabled = true
        binding.webView.loadUrl(
            (application as? DemoApplication)?.getWebsiteUrl() ?: DEFAULT_WEBSITE_URL
        )

        onBackPressedDispatcher.addCallback {
            binding.webView.apply {
                if (canGoBack()) {
                    goBack()
                } else {
                    finish()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}