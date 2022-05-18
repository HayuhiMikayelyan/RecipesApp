package com.example.recipes.UI.Fragment.OnBoarding.Screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.R


class OnBoardingFragment3 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_on_boarding3, container, false)

        view.findViewById<TextView>(R.id.btn_next_info3).setOnClickListener {
            findNavController().navigate(R.id.action_viewPagerFragment_to_signInFragment)
            onBoardingFinished()
        }

        return view
    }


    private fun onBoardingFinished(){
        val sharedPref = requireActivity().getSharedPreferences("onBoarding",Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished",true)
        editor.apply()
    }
}