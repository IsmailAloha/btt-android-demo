package com.bluetriangle.bluetriangledemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bluetriangle.analytics.Tracker
import com.bluetriangle.bluetriangledemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fields = Tracker.getInstance().globalFields
        with(binding) {
            siteIdEditText.setText(SITE_ID)
            globalUserIdEditText.setText(fields["gID"])
            sessionIdEditText.setText(fields["sID"])
        }
    }
}