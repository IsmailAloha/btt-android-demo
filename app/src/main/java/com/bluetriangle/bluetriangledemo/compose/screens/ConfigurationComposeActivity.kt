package com.bluetriangle.bluetriangledemo.compose.screens

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bluetriangle.bluetriangledemo.ConfigurationViewModel
import com.bluetriangle.bluetriangledemo.R
import com.bluetriangle.bluetriangledemo.compose.theme.BlueTriangleComposeDemoTheme
import com.bluetriangle.bluetriangledemo.compose.theme.outlineVariant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfigurationComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel by viewModels<ConfigurationViewModel>()
            BlueTriangleComposeDemoTheme {
                Scaffold(topBar = {
                    TopAppBar(title = { Text(text = getString(R.string.configuration)) },
                        navigationIcon = {
                            IconButton(onClick = {
                                onBackPressedDispatcher.onBackPressed()
                            }, modifier = Modifier.padding(8.dp)) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    "Back",
                                    tint = MaterialTheme.colors.onPrimary
                                )
                            }
                        })
                }, floatingActionButton = {
                    ExtendedFloatingActionButton(text = {
                        Text(
                            text = getString(R.string.apply), color = Color.Black
                        )
                    }, icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_done_24),
                            tint = Color.Black,
                            contentDescription = getString(R.string.apply)
                        )
                    }, onClick = { viewModel.onApplyClicked() })
                }) {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        Card(
                            elevation = 0.dp,
                            modifier = Modifier.padding(16.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colors.outlineVariant)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(it)
                                    .fillMaxSize()
                            ) {
                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = getString(R.string.configuration_settings),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                ConfigurationSwitch(
                                    R.string.keep_default_configurations,
                                    MutableLiveData(false),
                                    viewModel.isDefault,
                                    viewModel::onKeepDefaultChanged
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                Divider(color = Color.Black, thickness = 1.dp)
                                Spacer(modifier = Modifier.height(14.dp))
                                ConfigurationSwitch(
                                    R.string.screen_tracking,
                                    viewModel.isDefault,
                                    viewModel.isScreenTrackingEnabled,
                                    viewModel::onScreenTrackingChanged
                                )
                                ConfigurationSwitch(
                                    R.string.launch_time,
                                    viewModel.isDefault,
                                    viewModel.isLaunchTimeEnabled,
                                    viewModel::onLaunchTimeChanged
                                )
                                ConfigurationSwitch(
                                    R.string.performance_monitoring,
                                    viewModel.isDefault,
                                    viewModel.isPerformanceMonitoringEnabled,
                                    viewModel::onPerformanceMonitoringChanged
                                )
                                ConfigurationSwitch(
                                    R.string.crash_tracking,
                                    viewModel.isDefault,
                                    viewModel.isCrashTrackingEnabled,
                                    viewModel::onCrashTrackingChanged
                                )
                                ConfigurationSwitch(
                                    R.string.anr_tracking,
                                    viewModel.isDefault,
                                    viewModel.isANRTrackingEnabled,
                                    viewModel::onANRTrackingChanged
                                )
                                ConfigurationSwitch(
                                    R.string.memory_warning,
                                    viewModel.isDefault,
                                    viewModel.isMemoryWarningEnabled,
                                    viewModel::onMemoryWarningChanged
                                )
                                ConfigurationSwitch(
                                    R.string.network_capturing,
                                    viewModel.isDefault,
                                    viewModel.isNetworkCapturingEnabled,
                                    viewModel::onNetworkCapturingChanged
                                )
                                ConfigurationSwitch(
                                    R.string.network_state_tracking,
                                    viewModel.isDefault,
                                    viewModel.isNetworkStateTrackingEnabled,
                                    viewModel::onNetworkStateTrackingChanged
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                            }
                        }
                        Card(
                            elevation = 0.dp,
                            modifier = Modifier.padding(16.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colors.outlineVariant)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(it)
                                    .fillMaxSize()
                            ) {
                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = getString(R.string.launch_configuration),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                ConfigurationSwitch(
                                    R.string.configure_on_launch,
                                    MutableLiveData(false),
                                    viewModel.isConfigureOnLaunch,
                                    viewModel::onConfigureOnLaunchChanged
                                )
                                ConfigurationSwitch(
                                    R.string.add_delay,
                                    MutableLiveData(false),
                                    viewModel.isAddDelay,
                                    viewModel::onAddDelayChanged
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(60.dp))
                    }
                }
            }
        }
    }

    @Composable
    private fun ConfigurationSwitch(
        label: Int,
        defaultLiveData: LiveData<Boolean>,
        value: LiveData<Boolean>,
        onChange: (Boolean) -> Unit
    ) {
        val checked by value.observeAsState(false)
        val default by defaultLiveData.observeAsState(false)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = !default) { onChange(!checked) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = getString(label),
                color = if (default) colorResource(id = R.color.lightGrey) else colorResource(id = R.color.text_color),
                modifier = Modifier
                    .padding(16.dp, 2.dp)
                    .weight(1f)
            )
            Switch(
                checked = checked,
                enabled = !default,
                modifier = Modifier.padding(end = 16.dp),
                onCheckedChange = onChange
            )
        }
    }

}