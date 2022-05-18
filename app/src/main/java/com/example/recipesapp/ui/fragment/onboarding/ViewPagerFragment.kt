package com.example.recipes.UI.Fragment.OnBoarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.recipes.UI.Fragment.OnBoarding.Screens.OnBoardingFragment1
import com.example.recipes.UI.Fragment.OnBoarding.Screens.OnBoardingFragment2
import com.example.recipes.UI.Fragment.OnBoarding.Screens.OnBoardingFragment3
import com.example.recipesapp.R


class ViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)
        val fragmentList = arrayListOf(
            OnBoardingFragment1(),
            OnBoardingFragment2(),
            OnBoardingFragment3()
        )

        val adapter = ViewPagerAdapter(fragmentList,requireActivity().supportFragmentManager,lifecycle)

        view.findViewById<ViewPager2>(R.id.view_pager).adapter = adapter

        return view
    }


}