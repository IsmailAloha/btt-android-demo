package com.bluetriangle.bluetriangledemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bluetriangle.bluetriangledemo.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    init {
        lifecycleScope.launchWhenStarted {
            viewModel.request.collect {
                val displayRequest = DisplayRequestFragment().apply {
                    timerFields = it
                }
                displayRequest.show(supportFragmentManager, null)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {

            siteIdEditText.setText(viewModel.siteId.value)
            globalUserIdEditText.setText(viewModel.globalUserId.value)
            sessionIdEditText.setText(viewModel.sessionId.value)

            // Global
            siteIdEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.siteId.value = text.toString()
            }
            globalUserIdEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.globalUserId.value = text.toString()
            }
            sessionIdEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.sessionId.value = text.toString()
            }

            // Timer: A
            pageNameEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.timerConfiguration.value?.pageName = text.toString()
            }
            trafficSegmentNameEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.timerConfiguration.value?.trafficSegmentName = text.toString()
            }
            abTestIdEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.timerConfiguration.value?.abTestId = text.toString()
            }
            contentGroupNameEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.timerConfiguration.value?.contentGroupName = text.toString()
            }
            urlEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.timerConfiguration.value?.url = text.toString()
            }
            referrerEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.timerConfiguration.value?.referrer = text.toString()
            }

            // Campaign
            campaignNameEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.timerConfiguration.value?.campaignName = text.toString()
            }
            campaignSourceEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.timerConfiguration.value?.campaignSource = text.toString()
            }
            campaignMediumEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.timerConfiguration.value?.campaignMedium = text.toString()
            }

            // Timer: B
            brandValueEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.timerConfiguration.value?.brandValue = text.toString().toDoubleOrNull()
            }
            pageValueEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.timerConfiguration.value?.pageValue = text.toString().toDoubleOrNull()
            }
            cartValueEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.timerConfiguration.value?.cartValue = text.toString().toDoubleOrNull()
            }
            orderNumberEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.timerConfiguration.value?.orderNumber = text.toString()
            }
            orderTimeEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.timerConfiguration.value?.orderTime = text.toString().toLongOrNull()
            }
            timeOnPageEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.timerConfiguration.value?.timeOnPage = text.toString().toLongOrNull()
            }

            viewModel.timerConfiguration.observe(this@MainActivity) {
                pageNameEditText.setText("")
                trafficSegmentNameEditText.setText("")
                abTestIdEditText.setText("")
                contentGroupNameEditText.setText("")
                urlEditText.setText("")
                referrerEditText.setText("")
                campaignNameEditText.setText("")
                campaignSourceEditText.setText("")
                campaignMediumEditText.setText("")
                brandValueEditText.setText("")
                pageValueEditText.setText("")
                cartValueEditText.setText("")
                orderNumberEditText.setText("")
                orderTimeEditText.setText("")
                timeOnPageEditText.setText("")
            }

            submitButton.setOnClickListener { viewModel.submit() }
            clearButton.setOnClickListener { viewModel.clear() }
        }
    }
}