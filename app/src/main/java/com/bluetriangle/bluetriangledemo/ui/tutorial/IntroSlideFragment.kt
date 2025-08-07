package com.bluetriangle.bluetriangledemo.ui.tutorial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.bluetriangle.bluetriangledemo.R

class IntroSlideFragment(val title: String, val description: String) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro_slide, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val slideTitleView = view.findViewById<TextView>(R.id.slide_title)
        val slideDescriptionView = view.findViewById<TextView>(R.id.slide_description)

        slideTitleView.text = title
        slideDescriptionView.text = HtmlCompat.fromHtml(description.replace("\n", "<br/>"), HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

}