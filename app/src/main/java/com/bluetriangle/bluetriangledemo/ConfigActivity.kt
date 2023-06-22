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
        binding = ActivityConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val savedId = DemoApplication.tinyDB.getString("BttSiteId")
        if (!savedId.isNullOrBlank())
            binding.siteIdEditText.setText(savedId)
        else
            binding.siteIdEditText.setText(SITE_ID)

        binding.btnSave.setOnClickListener {
            val siteId = binding.siteIdEditText.text.toString()
            if (siteId.isBlank()) {
                Snackbar.make(it, "Please enter SiteId", Snackbar.LENGTH_LONG).show()
            } else {
                DemoApplication.tinyDB.setString("BttSiteId", siteId)
                (application as DemoApplication).intTracker(siteId)

                if(BuildConfig.FLAVOR == "compose") {
                    startActivity(Intent(this, ComposeStoreActivity::class.java))
                } else {
                    startActivity(Intent(this, StoreActivity::class.java))
                }
            }
        }
    }
}