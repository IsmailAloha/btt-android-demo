package com.bluetriangle.bluetriangledemo.ui.tutorial

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bluetriangle.bluetriangledemo.R
import com.bluetriangle.bluetriangledemo.layout.StoreActivity
import com.bluetriangle.bluetriangledemo.utils.introSlides

class AboutAppFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_app_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)

        val slides = introSlides.map {
            IntroSlideFragment(it.title, it.description)
        }

        viewPager.adapter = SlideAdapter(requireActivity(), slides)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                view.updateDots(position)
            }
        })

        view.setupIndicatorDots(slides.size)
        val doneButton = view.findViewById<Button>(R.id.done_button)
        doneButton.setOnClickListener {
            startActivity(Intent(context, StoreActivity::class.java))
            activity?.finish()
        }
    }

    private lateinit var dots: Array<ImageView?>

    private fun View.setupIndicatorDots(count: Int) {
        val dotsLayout: LinearLayout = findViewById(R.id.dotsIndicator)
        dots = arrayOfNulls(count)
        dotsLayout.removeAllViews()

        for (i in 0 until count) {
            dots[i] = ImageView(context)
            dots[i]?.setImageDrawable(
                ContextCompat.getDrawable(context, R.drawable.dot_inactive)
            )

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            dotsLayout.addView(dots[i], params)
        }

        // Set first dot as active
        if (dots.isNotEmpty())
            dots[0]?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.dot_active))
    }

    private fun View.updateDots(position: Int) {
        for (i in dots.indices) {
            dots[i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    if (i == position) R.drawable.dot_active else R.drawable.dot_inactive
                )
            )
        }
    }


    class SlideAdapter(fragmentActivity: FragmentActivity, val fragments: List<Fragment>): FragmentStateAdapter(fragmentActivity) {
        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }

        override fun getItemCount() = fragments.size

    }
}