package com.bluetriangle.bluetriangledemo.ui.tutorial

import android.os.Bundle
import android.text.Spanned
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import androidx.core.text.HtmlCompat
import com.bluetriangle.bluetriangledemo.R

class IntroSlideFragment(@DrawableRes val imageRes: Int, val title: String, val description: Spanned) : Fragment() {

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
        val slideImage = view.findViewById<ImageView>(R.id.slide_image)
        val slideDescriptionView = view.findViewById<TextView>(R.id.slide_description)

        slideImage.setImageResource(imageRes)
        slideTitleView.text = title
        slideDescriptionView.text = description
    }

}