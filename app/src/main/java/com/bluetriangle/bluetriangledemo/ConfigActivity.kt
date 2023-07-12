package com.bluetriangle.bluetriangledemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bluetriangle.bluetriangledemo.compose.screens.ComposeStoreActivity
import com.bluetriangle.bluetriangledemo.databinding.ActivityConfigBinding
import com.google.android.material.snackbar.Snackbar

class ConfigActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfigBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val savedId = DemoApplication.tinyDB.getString("BttSiteId")
        val savedANRDetection = DemoApplication.tinyDB.getBoolean("BttAnrDetection", true)
        val savedScreenTracking = DemoApplication.tinyDB.getBoolean("BttScreenTracking", true)
        binding.anrDetectionCheckbox.isChecked = savedANRDetection
        binding.screenTrackingCheckbox.isChecked = savedScreenTracking

        binding.siteIdEditText.setText(savedId)

        binding.btnSave.setOnClickListener {
            val siteId = binding.siteIdEditText.text.toString()
            if (siteId.isBlank()) {
                Snackbar.make(it, "Please enter SiteId", Snackbar.LENGTH_LONG).show()
            } else {
                val anrDetection = binding.anrDetectionCheckbox.isChecked
                val screenTracking = binding.screenTrackingCheckbox.isChecked
                DemoApplication.tinyDB.setString("BttSiteId", siteId)
                DemoApplication.tinyDB.setBoolean("BttAnrDetection", anrDetection)
                DemoApplication.tinyDB.setBoolean("BttScreenTracking", screenTracking)

                (application as DemoApplication).initTracker(
                    siteId,
                    anrDetection,
                    screenTracking
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