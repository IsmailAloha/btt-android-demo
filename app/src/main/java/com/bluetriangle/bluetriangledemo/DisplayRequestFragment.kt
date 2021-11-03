package com.bluetriangle.bluetriangledemo

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bluetriangle.bluetriangledemo.databinding.FragmentDisplayRequestBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.StringBuilder

class DisplayRequestFragment : BottomSheetDialogFragment() {

    lateinit var timerFields: HashMap<String,String>
    private lateinit var binding: FragmentDisplayRequestBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDisplayRequestBinding.inflate(inflater, container, false)
        with(binding) {
            requestText.text = getTimerFieldsString()
            return root
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from(bottomSheet).state  = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog
    }

    private fun getTimerFieldsString() =
        StringBuilder().apply {
            append("{")
            timerFields.forEach { (key, value) ->
                append("\n   \"$key\": \"$value\",")
            }
            append("\n}")
        }
            .toString()
}