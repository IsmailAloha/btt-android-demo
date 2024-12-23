package com.bluetriangle.bluetriangledemo.layout

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bluetriangle.analytics.Tracker
import com.bluetriangle.bluetriangledemo.ConfigurationManager
import com.bluetriangle.bluetriangledemo.DemoApplication
import com.bluetriangle.bluetriangledemo.R
import com.bluetriangle.bluetriangledemo.databinding.ActivityStoreBinding
import com.bluetriangle.bluetriangledemo.tests.MemoryMonitor
import com.bluetriangle.bluetriangledemo.utils.copyToClipboard
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        (application as DemoApplication).memoryMonitor.memoryWarningListener = object:MemoryMonitor.MemoryWarningListener {
            override fun onMemoryWarning(memoryWarning: MemoryMonitor.MemoryWarning) {
                runOnUiThread {
                    AlertDialog.Builder(this@StoreActivity)
                        .setTitle(R.string.memory_warning_title)
                        .setMessage(R.string.memory_warning_message)
                        .setPositiveButton(R.string.memory_warning_ok) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }
        }

        ConfigurationManager.sessionId.observe(this) {
            binding.sessionid.text = it
        }

        binding.sessionid.setOnClickListener {
            it.context.copyToClipboard("Session ID", Tracker.instance?.configuration?.sessionId ?: "")
        }

        val navController = findNavController(R.id.nav_host_fragment_activity_store)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_products, R.id.navigation_cart, R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

}