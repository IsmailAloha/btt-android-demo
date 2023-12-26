package com.bluetriangle.bluetriangledemo.compose.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bluetriangle.analytics.Timer
import com.bluetriangle.analytics.compose.BttTimerEffect
import com.bluetriangle.bluetriangledemo.AboutActivity
import com.bluetriangle.bluetriangledemo.DemoApplication
import com.bluetriangle.bluetriangledemo.R
import com.bluetriangle.bluetriangledemo.ui.settings.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = viewModel()) {
    val context = LocalContext.current
    val values = listOf(
        context.getString(R.string.android_version) to viewModel.androidVersionName,
        context.getString(R.string.sdk_version) to viewModel.sdkVersion,
        context.getString(R.string.app_version) to viewModel.appVersion,
        context.getString(R.string.app_flavor) to viewModel.flavor,
        context.getString(R.string.site_id) to viewModel.siteId.toString(),
        context.getString(R.string.session_id) to (viewModel.sessionId ?: ""),
        context.getString(R.string.anr_enabled) to viewModel.anrEnabled,
        context.getString(R.string.screen_tracking_enabled) to viewModel.screenTrackingEnabled,
    )
    val scrollState = rememberScrollState()
    var websiteUrlDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }
    BttTimerEffect(screenName = "Settings Tab")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        values.map {
            InfoItem(it.first, it.second)
        }
        Button(onClick = {
            viewModel.testManualTimer()
        }) {
            Text(text = stringResource(R.string.test_manual_timer))
        }
        Row {
            Button(onClick = {
                context.startActivity(Intent(context, AboutActivity::class.java))
            }) {
                Text(text = stringResource(R.string.about_us))
            }

            Icon(Icons.Filled.Settings, contentDescription = "Settings",
                Modifier
                    .padding(top = 8.dp, start = 8.dp)
                    .clickable {
                        websiteUrlDialogOpen = true
                    })
        }
        if (websiteUrlDialogOpen) {
            WebsiteDialog {
                websiteUrlDialogOpen = false
            }
        }
    }
}

@Composable
fun WebsiteDialog(onDismiss: () -> Unit) {
    val app = (LocalContext.current.applicationContext as? DemoApplication)
    val currentUrl = app?.getWebsiteUrl()
    var websiteUrl by rememberSaveable {
        mutableStateOf(currentUrl ?: "")
    }
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp, 32.dp)) {
                TextField(value = websiteUrl, modifier = Modifier.fillMaxWidth(), singleLine = true, onValueChange = {
                    websiteUrl = it
                })
                Button(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(0.dp, 0.dp, 4.dp, 4.dp), onClick = {
                    app?.setWebsiteUrl(websiteUrl)
                    onDismiss()
                }) {
                    Text(text = stringResource(id = R.string.done))
                }
            }
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onSurface
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = TextStyle(
                fontSize = 16.sp,
                color = MaterialTheme.colors.onSurface
            )
        )
    }
}