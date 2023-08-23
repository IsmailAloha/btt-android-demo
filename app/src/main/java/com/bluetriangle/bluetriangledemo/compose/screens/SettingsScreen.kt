package com.bluetriangle.bluetriangledemo.compose.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bluetriangle.analytics.Timer
import com.bluetriangle.analytics.compose.BttTimerEffect
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
        context.getString(R.string.session_id) to (viewModel.sessionId?:""),
        context.getString(R.string.anr_enabled) to viewModel.anrEnabled,
        context.getString(R.string.screen_tracking_enabled) to viewModel.screenTrackingEnabled,
    )
    BttTimerEffect(screenName = "Settings Tab")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
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