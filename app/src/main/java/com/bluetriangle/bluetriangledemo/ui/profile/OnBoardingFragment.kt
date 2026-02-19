package com.bluetriangle.bluetriangledemo.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bluetriangle.analytics.Timer
import com.bluetriangle.bluetriangledemo.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OnBoardingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_on_boarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = view.findViewById<TabLayout>(R.id.signup_login_tabs)
        val viewPager = view.findViewById<ViewPager2>(R.id.signup_login_container)

        val customTimer = Timer("CustomTimer", "")
        customTimer.apply {
            setAbTestIdentifier("demoapp_variant_A")
            setCampaignMedium("push")
            setCampaignName("winter_sale_2026")
            setCampaignSource("newsletter_feb")
        }

        customTimer.start()
        lifecycleScope.launch {
            delay(2000)
            customTimer.submit()
        }

        viewPager.adapter = TabsPagerAdapter(this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setContentDescription(
                when(position) {
                    0 -> R.string.a11y_tab_signup
                    else -> R.string.a11y_tab_login
                }
            )
            tab.text = when (position) {
                0 -> "Sign Up"
                else -> "Login"
            }
        }.attach()
    }


    class TabsPagerAdapter(
        fragment: Fragment
    ) : FragmentStateAdapter(fragment) {

        private val fragments = listOf(
            SignupFragment(),
            LoginFragment()
        )

        override fun getItemCount() = fragments.size

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }
}