package com.bluetriangle.bluetriangledemo

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.bluetriangle.analytics.BTTWebViewTracker
import com.bluetriangle.bluetriangledemo.DemoApplication.Companion.DEFAULT_TAG_URL
import com.bluetriangle.bluetriangledemo.databinding.ActivityAboutBinding
import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter


class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle(R.string.hybrid_demo)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        binding.webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }

            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
                BTTWebViewTracker.onLoadResource(view, url)
            }

            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }
        }
        binding.webView.settings.javaScriptEnabled = true
        WebView.setWebContentsDebuggingEnabled(true)
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.allowFileAccess = true

        try {
            var webSiteContent = String(assets.open("template.html").readBytes())
            val tagUrl = (application as? DemoApplication)?.getTagUrl() ?: DEFAULT_TAG_URL
            webSiteContent = webSiteContent.replace("<!--- SCRIPT_TAG_GOES_HERE --->", "<script type=\"text/javascript\" id=\"\" src=\"https://$tagUrl\"></script>")
            val file = File(filesDir, "index.html")
            if(!file.exists()) {
                file.createNewFile()
            }
            file.writeText(webSiteContent)
            val websiteLocalURL = "file://${file.absolutePath}"
            Log.d("AboutActivityWebContent", "URL: $websiteLocalURL")
            binding.webView.loadUrl(websiteLocalURL)
        } catch (e: IOException) {
            val stackTraceWriter = StringWriter()
            e.printStackTrace(PrintWriter(stackTraceWriter))
            Log.d("AboutActivityWebContent", "${e::class.java.simpleName}(\"${e.message}\") : ${stackTraceWriter}")
        }

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