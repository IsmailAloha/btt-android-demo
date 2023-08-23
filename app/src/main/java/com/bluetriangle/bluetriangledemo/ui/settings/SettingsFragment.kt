package com.bluetriangle.bluetriangledemo.ui.settings

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bluetriangle.bluetriangledemo.BuildConfig
import com.bluetriangle.bluetriangledemo.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        _binding?.androidVersionNameValue?.text = settingsViewModel.androidVersionName
        _binding?.appVersionValue?.text = settingsViewModel.appVersion
        _binding?.flavorValue?.text = settingsViewModel.flavor
        _binding?.sdkVersionValue?.text = settingsViewModel.sdkVersion
        _binding?.siteIdValue?.text = settingsViewModel.siteId
        _binding?.sessionIDValueText?.text = settingsViewModel.sessionId
        _binding?.anrEnabledValue?.text = settingsViewModel.anrEnabled
        _binding?.screenTrackingEnabledValue?.text = settingsViewModel.screenTrackingEnabled
        _binding?.testManualTimer?.setOnClickListener {
            settingsViewModel.testManualTimer()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}