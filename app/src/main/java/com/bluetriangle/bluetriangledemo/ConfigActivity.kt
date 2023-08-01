package com.bluetriangle.bluetriangledemo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bluetriangle.bluetriangledemo.compose.screens.ComposeStoreActivity
import com.bluetriangle.bluetriangledemo.databinding.ActivityConfigBinding
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.UUID

class ConfigActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfigBinding
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val savedId = DemoApplication.tinyDB.getString(SITE_ID, DEFAULT_SITE_ID)
        val savedANRDetection = DemoApplication.tinyDB.getBoolean(KEY_ANR_ENABLED, true)
        val savedScreenTracking = DemoApplication.tinyDB.getBoolean(KEY_SCREEN_TRACKING_ENABLED, true)
        binding.siteIdEditText.setText(savedId)
        binding.anrDetectionCheckbox.isChecked = savedANRDetection
        binding.screenTrackingCheckbox.isChecked = savedScreenTracking
        binding.sessionIdText.setText(SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().time))

        binding.btnSave.setOnClickListener {
            val siteId = binding.siteIdEditText.text.toString()
            if (siteId.isBlank()) {
                Snackbar.make(it, "Please enter SiteId", Snackbar.LENGTH_LONG).show()
            } else {
                var sessionId = binding.sessionIdText.text.toString()
                if(sessionId.length == 8) {
                    sessionId = UUID.randomUUID().toString()
                }
                val anrDetection = binding.anrDetectionCheckbox.isChecked
                val screenTracking = binding.screenTrackingCheckbox.isChecked

                DemoApplication.tinyDB.setString(KEY_SITE_ID, siteId)
                DemoApplication.tinyDB.setBoolean(KEY_ANR_ENABLED, anrDetection)
                DemoApplication.tinyDB.setBoolean(KEY_SCREEN_TRACKING_ENABLED, screenTracking)
                DemoApplication.tinyDB.setString(KEY_SESSION_ID, sessionId)

                (application as DemoApplication).initTracker(
                    siteId,
                    anrDetection,
                    screenTracking,
                    sessionId
                )

                if(BuildConfig.FLAVOR == "compose") {
                    startActivity(Intent(this, ComposeStoreActivity::class.java))
                } else {
                    startActivity(Intent(this, StoreActivity::class.java))
                }
            }
        }
    }
}