package com.bluetriangle.bluetriangledemo.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.webkit.WebView
import android.widget.ImageView
import android.widget.Toast
import com.bluetriangle.bluetriangledemo.DemoApplication
import com.bluetriangle.bluetriangledemo.DemoApplication.Companion.DEMO_WEBSITE_URL
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter

fun Context.dp(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
}

fun ImageView.loadImage(url:String?) {
    Glide.with(context)
        .load(url)
        .transform(
            MultiTransformation(
                CenterCrop(),
                RoundedCorners(context.dp(8))
            )
        )
        .into(this)
}
//WebView: 8354836313254369909//After 30 minutes: 8354836313254369909
//WebView: 5159742485216640278//After 30 minutes:5159742485216640278
fun Context.generateDemoWebsiteFromTemplate() {
    var webSiteContent = String(assets.open("template.html").readBytes())
    val tagUrl = (applicationContext as? DemoApplication)?.getTagUrl() ?: DemoApplication.DEFAULT_TAG_URL
    Log.d("HybridDemoWebsiteTag", "Generating demo website: $tagUrl")
    webSiteContent = webSiteContent.replace(
        "<!--- SCRIPT_TAG_GOES_HERE --->",
        "<script type=\"text/javascript\" id=\"\" src=\"https://$tagUrl\"></script>"
    )
    val file = File(filesDir, "index.html")
    if (!file.exists()) {
        file.createNewFile()
    }
    file.writeText(webSiteContent)
}

fun Context.copyToClipboard(label:String, value: String) {
    val clipBoardManager = (getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)?:return
    clipBoardManager.setPrimaryClip(ClipData.newPlainText(label, value))
    Toast.makeText(this, "Copied: $value", Toast.LENGTH_SHORT).show()
}