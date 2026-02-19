package com.bluetriangle.bluetriangledemo.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bluetriangle.bluetriangledemo.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel by viewModels<ProfileViewModel>(ownerProducer = { requireActivity() })

        val username = view.findViewById<TextView>(R.id.username)
        val profileType = view.findViewById<TextView>(R.id.profileType)
        val logoutButton = view.findViewById<TextView>(R.id.logout)

        username.text = viewModel.loggedInUser.value
        profileType.text = if(viewModel.isPremium.value) "Premium User" else "Normal User"

        logoutButton.setOnClickListener {
            viewModel.logout()
        }
    }

}