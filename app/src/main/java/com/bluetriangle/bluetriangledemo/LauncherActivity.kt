package com.bluetriangle.bluetriangledemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bluetriangle.bluetriangledemo.compose.screens.ComposeStoreActivity

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        supportActionBar?.hide()
        DemoApplication.checkAndRunLaunchScenario(SCENARIO_ACTIVITY_CREATE)
    }

    override fun onStart() {
        super.onStart()
        DemoApplication.checkAndRunLaunchScenario(SCENARIO_ACTIVITY_START)
    }

    override fun onResume() {
        super.onResume()
        DemoApplication.checkAndRunLaunchScenario(SCENARIO_ACTIVITY_RESUME)

        if(BuildConfig.FLAVOR == "compose") {
            startActivity(Intent(this, ComposeStoreActivity::class.java))
        } else {
            startActivity(Intent(this, StoreActivity::class.java))
        }
        finish()
    }
}