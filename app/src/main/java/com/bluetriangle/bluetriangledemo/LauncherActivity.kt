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

        val launchTest = DemoApplication.tinyDB.getBoolean(KEY_LAUNCH_TEST)

        if(launchTest) {
            if(BuildConfig.FLAVOR == "compose") {
                startActivity(Intent(this, ComposeStoreActivity::class.java))
            } else {
                startActivity(Intent(this, StoreActivity::class.java))
            }
        } else {
            startActivity(Intent(this, ConfigActivity::class.java))
        }
        finish()
    }
}