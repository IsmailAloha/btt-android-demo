package com.bluetriangle.bluetriangledemo.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.viewModels
import com.bluetriangle.bluetriangledemo.R
import com.google.android.material.chip.ChipGroup

class SignupFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel by viewModels<ProfileViewModel>(ownerProducer = { requireActivity() })

        val profileType = view.findViewById<ChipGroup>(R.id.profileType)
        val username = view.findViewById<EditText>(R.id.username)
        val loginButton = view.findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            viewModel.login(username.text.toString(), profileType.checkedChipId == R.id.premium)
        }
    }
}