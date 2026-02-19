package com.bluetriangle.bluetriangledemo.layout

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bluetriangle.bluetriangledemo.R
import com.bluetriangle.bluetriangledemo.ui.profile.LoginFragment
import com.bluetriangle.bluetriangledemo.ui.profile.OnBoardingFragment
import com.bluetriangle.bluetriangledemo.ui.profile.ProfileFragment
import com.bluetriangle.bluetriangledemo.ui.profile.ProfileViewModel
import com.bluetriangle.bluetriangledemo.ui.profile.SignupFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class ProfileLayoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()
        setContentView(R.layout.activity_profile_layout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val viewModel by viewModels<ProfileViewModel>()

        lifecycleScope.launch {
            viewModel.loggedInUser.collect {
                if(it == null) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, OnBoardingFragment())
                        .commit()
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, ProfileFragment())
                        .commit()
                }
            }
        }
    }
}