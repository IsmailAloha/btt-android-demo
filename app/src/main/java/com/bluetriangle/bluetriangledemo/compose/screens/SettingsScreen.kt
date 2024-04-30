package com.bluetriangle.bluetriangledemo.compose.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bluetriangle.analytics.compose.BttTimerEffect
import com.bluetriangle.bluetriangledemo.DemoApplication
import com.bluetriangle.bluetriangledemo.R
import com.bluetriangle.bluetriangledemo.ui.settings.SettingsViewModel
import com.bluetriangle.bluetriangledemo.utils.copyToClipboard

class SettingsInfo(val label: String, val value: String, val copyAvailable: Boolean = false)

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = viewModel()) {
    val context = LocalContext.current
    val values = listOf(
//        SettingsInfo(context.getString(R.string.android_version), viewModel.androidVersionName),
//        SettingsInfo(context.getString(R.string.sdk_version), viewModel.sdkVersion),
//        SettingsInfo(context.getString(R.string.app_version), viewModel.appVersion),
//        SettingsInfo(context.getString(R.string.app_flavor), viewModel.flavor),
        SettingsInfo(context.getString(R.string.site_id), viewModel.siteId.toString()),
        SettingsInfo(context.getString(R.string.session_id), (viewModel.sessionId ?: ""), true),
//        SettingsInfo(context.getString(R.string.anr_enabled), viewModel.anrEnabled),
//        SettingsInfo(
//            context.getString(R.string.screen_tracking_enabled),
//            viewModel.screenTrackingEnabled
//        ),
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
        Image(
            painter = painterResource(id = R.drawable.icon_compose),
            contentDescription = "Compose Icon",
            modifier = Modifier
                .size(72.dp)
                .align(Alignment.CenterHorizontally)
        )
        AppInfo(viewModel)
        values.map {
            InfoItem(it)
        }
        Button(onClick = {
            viewModel.testManualTimer()
        }) {
            Text(text = stringResource(R.string.test_manual_timer))
        }
        Row {
            Button(onClick = {
                context.startActivity(Intent(context, ComposeAboutActivity::class.java))
            }) {
                Text(text = stringResource(R.string.hybrid_demo))
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {
                websiteUrlDialogOpen = true
            }) {
                Text(text = stringResource(R.string.tag_url))
            }
        }

        Button(onClick = {
            context.startActivity(Intent(context, ConfigurationComposeActivity::class.java))
        }) {
            Text(text = stringResource(R.string.configuration))
        }

        if (websiteUrlDialogOpen) {
            WebsiteDialog {
                websiteUrlDialogOpen = false
            }
        }
    }
}

@Composable
fun AppInfo(viewModel: SettingsViewModel) {
    Column(Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colors.onSurface
            ),
            text = stringResource(id = R.string.app_name)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = TextStyle(
                fontStyle = FontStyle.Italic,
                fontSize = 16.sp,
                color = MaterialTheme.colors.onSurface
            ),
            text = "This app is built with Compose"
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colors.onSurface
            ),
            text = "Version"
        )
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = TextStyle(
                fontSize = 16.sp,
                color = MaterialTheme.colors.onSurface
            ),
            text = viewModel.appVersion
        )
    }
}

@Composable
fun WebsiteDialog(onDismiss: () -> Unit) {
    val app = (LocalContext.current.applicationContext as? DemoApplication)
    val currentUrl = app?.getTagUrl()
    var tagUrl by rememberSaveable {
        mutableStateOf(currentUrl ?: "")
    }
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp, 32.dp)) {
                TextField(
                    value = tagUrl,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    onValueChange = {
                        tagUrl = it
                    })
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(0.dp, 0.dp, 4.dp, 4.dp),
                    onClick = {
                        app?.setTagUrl(tagUrl)
                        onDismiss()
                    }) {
                    Text(text = stringResource(id = R.string.done))
                }
            }
        }
    }
}

@Composable
fun InfoItem(settingsInfo: SettingsInfo) {
    val context = LocalContext.current
    Column {
        Text(
            text = settingsInfo.label,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onSurface
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = settingsInfo.value,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.onSurface
                )
            )
            if (settingsInfo.copyAvailable) {
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    context.copyToClipboard(settingsInfo.label, settingsInfo.value)
                }) {
                    Text(text = stringResource(id = R.string.copy), fontSize = 12.sp)
                }
            }
        }
    }
}
