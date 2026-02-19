package com.bluetriangle.bluetriangledemo.compose.screens

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.bluetriangle.bluetriangledemo.DemoApplication
import com.bluetriangle.bluetriangledemo.R
import com.bluetriangle.bluetriangledemo.compose.theme.BlueTriangleComposeDemoTheme
import com.bluetriangle.bluetriangledemo.compose.theme.outline
import com.bluetriangle.bluetriangledemo.utils.INTRO_SHOWN
import com.bluetriangle.bluetriangledemo.utils.introSlides
import kotlinx.coroutines.launch

class AppIntroComposeActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BlueTriangleComposeDemoTheme {
                val colorScheme = MaterialTheme.colors
                val view = LocalView.current
                val pagerState = rememberPagerState(
                    initialPage = 0,
                    pageCount = { introSlides.size }
                )
                val coroutineScope = rememberCoroutineScope()
                val context = LocalContext.current
                if (!view.isInEditMode) {
                    SideEffect {
                        val window = (view.context as Activity).window
                        window.statusBarColor = colorScheme.background.toArgb()
                        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                            true
                    }
                }
                Scaffold { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {
                        HorizontalPager(
                            modifier = Modifier.weight(1f),
                            state = pagerState,
                        ) { page ->

                            val scrollState = rememberScrollState()

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(scrollState)
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Spacer(Modifier.height(36.dp))
                                Image(
                                    painter = painterResource(id = introSlides[page].imageRes),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .width(200.dp)
                                        .height(200.dp), // set only height
                                    contentScale = ContentScale.Fit// fills width, keeps aspect ratio
                                )
                                Spacer(Modifier.height(20.dp))
                                Text(
                                    text = introSlides[page].title,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 32.sp
                                )
                                Spacer(Modifier.height(16.dp))
                                Text(
                                    text = introSlides[page].spannedDescription.toAnnotatedString(),
                                    fontSize = 18.sp,
                                    lineHeight = 22.sp
                                )
                                Spacer(Modifier.weight(1f))
//                            NavigationButtons(currentPage = page, totalPages = introSlides.size)
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                modifier = Modifier.semantics { contentDescription = getString(R.string.a11y_intro_prev)},
                                enabled = pagerState.canScrollBackward,
                                onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            }) {
                                Text(stringResource(R.string.prev).uppercase())
                            }
                            Spacer(Modifier.weight(1f))
                            PagerIndicator(
                                currentPage = pagerState.currentPage,
                                totalPages = introSlides.size
                            )
                            Spacer(Modifier.weight(1f))
                            TextButton(
                                modifier = Modifier.semantics { contentDescription = getString(R.string.a11y_intro_next)},
                                onClick = {
                                if(pagerState.canScrollForward) {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    }
                                } else {
                                    DemoApplication.tinyDB.setBoolean(INTRO_SHOWN, true)
                                    startActivity(Intent(context, ComposeStoreActivity::class.java))
                                    finish()
                                }
                            }) {
                                Text(stringResource(if(pagerState.canScrollForward) R.string.next else R.string.done).uppercase())
                            }
                        }

                        Spacer(Modifier.height(32.dp))
                    }


                }
            }
        }
    }

}

@Composable
fun PagerIndicator(modifier: Modifier = Modifier, currentPage: Int, totalPages: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        for (i in 0 until totalPages) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        if (i == currentPage) MaterialTheme.colors.secondary else MaterialTheme.colors.outline,
                        shape = CircleShape
                    )
            )
        }
    }
}

fun Spanned.toAnnotatedString(): AnnotatedString {
    return buildAnnotatedString {
        append(this@toAnnotatedString.toString())

        getSpans(0, length, Any::class.java).forEach { span ->
            val start = getSpanStart(span)
            val end = getSpanEnd(span)

            when (span) {
                is ForegroundColorSpan -> addStyle(
                    SpanStyle(color = Color(span.foregroundColor)),
                    start,
                    end
                )

                is StyleSpan -> {
                    when (span.style) {
                        Typeface.BOLD -> addStyle(
                            SpanStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
                            start,
                            end
                        )

                        Typeface.ITALIC -> addStyle(
                            SpanStyle(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                            start,
                            end
                        )
                    }
                }
                // Add more span mappings here as needed
            }
        }
    }
}