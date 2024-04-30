package com.bluetriangle.bluetriangledemo.compose.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.SwitchColors
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bluetriangle.bluetriangledemo.ConfigurationViewModel
import com.bluetriangle.bluetriangledemo.R
import com.bluetriangle.bluetriangledemo.compose.theme.BlueTriangleComposeDemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfigurationComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel by viewModels<ConfigurationViewModel>()
            BlueTriangleComposeDemoTheme {
                Scaffold(topBar = {
                    TopAppBar(
                        title = { Text(text = getString(R.string.configuration)) },
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
                },
                    floatingActionButton = {
                        ExtendedFloatingActionButton(
                            text = {
                                Text(
                                    text = getString(R.string.apply),
                                    color = Color.Black
                                )
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_done_24),
                                    tint = Color.Black,
                                    contentDescription = getString(R.string.apply)
                                )
                            },
                            onClick = { viewModel.onApplyClicked() })
                    }) {
                    Column(
                        modifier = Modifier
                            .padding(it)
                            .fillMaxSize()
                    ) {
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
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable { onChange(!checked) }, verticalAlignment = Alignment.CenterVertically
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