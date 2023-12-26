package com.bluetriangle.bluetriangledemo.ui.settings

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bluetriangle.bluetriangledemo.AboutActivity
import com.bluetriangle.bluetriangledemo.BuildConfig
import com.bluetriangle.bluetriangledemo.DemoApplication
import com.bluetriangle.bluetriangledemo.DemoApplication.Companion.DEFAULT_WEBSITE_URL
import com.bluetriangle.bluetriangledemo.R
import com.bluetriangle.bluetriangledemo.databinding.FragmentSettingsBinding
import com.google.android.material.textfield.TextInputEditText

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
        _binding?.aboutUs?.setOnClickListener {
            startActivity(Intent(requireContext(), AboutActivity::class.java))
        }
        _binding?.urlSettingsIcon?.setOnClickListener {
            showAboutUrlDialog()
        }
        return root
    }

    private fun showAboutUrlDialog() {
        val dialog = Dialog(requireContext(), R.style.AboutDialogWindowTheme)
        dialog.setContentView(R.layout.about_url_dialog)
        dialog.window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
        val doneButton = dialog.findViewById<Button>(R.id.doneButton)
        val urlField = dialog.findViewById<TextInputEditText>(R.id.websiteURLField)
        val app = (requireContext().applicationContext as? DemoApplication)
        val websiteUrl = app?.getWebsiteUrl()?:DEFAULT_WEBSITE_URL
        urlField.setText(websiteUrl)
        doneButton.setOnClickListener {
            app?.setWebsiteUrl(urlField.text.toString())
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}