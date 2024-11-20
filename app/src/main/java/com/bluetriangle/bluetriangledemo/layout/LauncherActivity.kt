package com.bluetriangle.bluetriangledemo.layout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.bluetriangle.bluetriangledemo.BuildConfig
import com.bluetriangle.bluetriangledemo.DemoApplication
import com.bluetriangle.bluetriangledemo.R
import com.bluetriangle.bluetriangledemo.compose.screens.ComposeStoreActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        supportActionBar?.hide()
        DemoApplication.checkAndAddDelay()
    }

    override fun onStart() {
        super.onStart()
        DemoApplication.checkAndAddDelay()
    }

    override fun onResume() {
        super.onResume()
        DemoApplication.checkAndAddDelay()

        if(BuildConfig.FLAVOR.contains("compose")) {
            startActivity(Intent(this, ComposeStoreActivity::class.java))
        } else {
            startActivity(Intent(this, StoreActivity::class.java))
        }
        finish()
    }
}