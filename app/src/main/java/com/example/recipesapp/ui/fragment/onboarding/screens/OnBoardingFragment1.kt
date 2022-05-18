package com.example.recipes.UI.Fragment.OnBoarding.Screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.example.recipesapp.R


class OnBoardingFragment1 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_on_boarding1, container, false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.view_pager)
        view.findViewById<TextView>(R.id.btn_next_info1).setOnClickListener {
            viewPager?.currentItem = 1
        }


        return view
    }

}