package com.bluetriangle.bluetriangledemo.compose.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bluetriangle.analytics.compose.BttTimerEffect
import com.bluetriangle.bluetriangledemo.R
import com.bluetriangle.bluetriangledemo.compose.theme.BlueTriangleComposeDemoTheme
import com.bluetriangle.bluetriangledemo.ui.profile.ProfileViewModel

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = viewModel()
) {
    BttTimerEffect("ProfileScreen")
    val scrollState = rememberScrollState()
    val loggedInUser by profileViewModel.loggedInUser.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .displayCutoutPadding()
            .padding(16.dp)
            .scrollable(scrollState, Orientation.Vertical),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Center
    ) {

        if (loggedInUser == null) {
            Card(modifier = Modifier.fillMaxWidth()) {
                LoginForm(profileViewModel)
            }
        } else {
            Card(
                Modifier.fillMaxWidth(),
                elevation = 0.dp,
                border = BorderStroke(1.dp, Color(0xFF999999))
            ) {
                UserInfoScreen(loggedInUser ?: "Unknown", profileViewModel)
            }
        }
    }

}

@Composable
fun UserInfoScreen(loggedInUser: String, profileViewModel: ProfileViewModel) {
    val isPremium by profileViewModel.isPremium.collectAsState(false)

    Column(modifier = Modifier
        .padding(20.dp, 24.dp)
        .fillMaxWidth()) {
        Text(
            text = loggedInUser,
            fontSize = 32.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (isPremium) "Premium User" else "Normal User",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = MaterialTheme.colors.primary
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            profileViewModel.logout()
        }) {
            Text(text = "Logout")
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoginForm(viewModel: ProfileViewModel) {
    var username by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }
    var isPremium by rememberSaveable {
        mutableStateOf(false)
    }
    val chipColors = ChipDefaults.filterChipColors(
        selectedBackgroundColor = MaterialTheme.colors.primary,
        selectedContentColor = MaterialTheme.colors.onPrimary
    )
    Column(Modifier.padding(16.dp)) {
        Row {
            FilterChip(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp),
                selected = !isPremium,
                colors = chipColors,
                shape = MaterialTheme.shapes.small,
                onClick = { isPremium = !isPremium }
            ) {
                Text(
                    text = stringResource(R.string.normal),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            FilterChip(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp),
                selected = isPremium,
                colors = chipColors,
                shape = MaterialTheme.shapes.small,
                onClick = { isPremium = !isPremium }
            ) {
                Text(
                    text = stringResource(R.string.premium),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.height(48.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = username,
            label = { Text(text = stringResource(R.string.username)) },
            onValueChange = {
                username = it
            })
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            label = { Text(text = stringResource(R.string.password)) },
            onValueChange = {
                password = it
            })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            viewModel.login(username, isPremium)
        }) {
            Text(text = stringResource(R.string.login))
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    BlueTriangleComposeDemoTheme {
        ProfileScreen(viewModel())
    }
}