package com.bluetriangle.bluetriangledemo

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bluetriangle.analytics.Tracker
import com.bluetriangle.bluetriangledemo.databinding.ActivityStoreBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        binding.sessionid.text = Tracker.instance!!.configuration.sessionId

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