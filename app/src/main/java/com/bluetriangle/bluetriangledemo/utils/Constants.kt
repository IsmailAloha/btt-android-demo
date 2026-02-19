package com.bluetriangle.bluetriangledemo.utils

import android.text.Spanned
import androidx.annotation.DrawableRes
import androidx.core.text.HtmlCompat
import com.bluetriangle.bluetriangledemo.BuildConfig
import com.bluetriangle.bluetriangledemo.R

const val ADD_TO_CART_LIMIT = 3
val DEFAULT_SITE_ID = BuildConfig.SITE_ID
const val MANUAL_TIMER_SEGMENT = "AndroidManualTimer"
const val INTRO_SHOWN = "intro_shown"

enum class BTTCustomVariables(val key:String) {
    User("CV1"),
    Premium("CV2"),
    ScreenWidth("CV3"),
    ScreenHeight("CV4"),
    FullStorySessionURL("FullStoryReplayLink")
}

enum class UserType(val type: String) {
    Guest("Guest"),
    Standard("Standard"),
    Premium("Premium")
}
class IntroSlideData(
    @DrawableRes val imageRes: Int,
    val title: String,
    val description: String
) {
    val spannedDescription: Spanned
        get() = HtmlCompat.fromHtml(description.replace("\n", "<br/>"), HtmlCompat.FROM_HTML_MODE_COMPACT)
}

val introSlides = listOf<IntroSlideData>(
    IntroSlideData(
        R.drawable.ecom_demo_image,
        "Welcome to Ecom Demo App",
        """This app is built for testing out the features of the Blue Triangle SDK for Android. It has built in support for generating scenarios for testing out features such as:
                   |   
                   |•&nbsp;&nbsp;Screen Tracking
                   |•&nbsp;&nbsp;Crash Tracking
                   |•&nbsp;&nbsp;ANR Detection
                   |
                   |and more.""".trimMargin()
    ),
    IntroSlideData(
        R.drawable.screen_tracking_image,
        "Screen Tracking",
        """The Blue Triangle SDK automatically tracks all screens for Layout based UI, for Jetpack Compose the app screens have manually been tagged to be tracked by the SDK. 
           |
           |Just browse through different screens, perform checkouts, etc that will generate some page views.""".trimMargin()),
    IntroSlideData(
        R.drawable.crash_image,
        "Crash Tracking",
        """There are two scenarios for generating crash in this app.
                |
                |<b>Scenario 1: Empty Cart Checkout</b>
                |A checkout without any products in the cart would simulate a crash.
                |
                |<b>Scenario 2: Cart Overflow</b>
                |Adding 5 or more distinct products to the cart and performing a checkout operation would result in a crash.
                |
                |<b>Note:</b> The crash will be captured and stored locally until the next launch of the app when it will be submitted to the backend servers.""".trimMargin()
    ),
    IntroSlideData(
        R.drawable.anr_image,
        "ANR Detection",
        """Application Not Responding (ANR) is a state when the app is unresponsive for a significant amount of time. The Blue Triangle SDK tracks such states and reports it. This app has some manufactured ANR scenarios built in.
                |
                |<b>Scenario 1: Remove a Product from Cart</b>
                |Add a product to the cart and then try removing it using the trash icon. That will result in the app getting hanged for several seconds resulting in an ANR state which will be reported.
                |
                |<b>Scenario 2: Continue Shopping</b>
                |Add some products to the cart and perform a checkout. A checkout confirmation screen appears with a "Continue Shopping" button. Click on it will result in the app getting into an ANR state.""".trimMargin()
    )
)