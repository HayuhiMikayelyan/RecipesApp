package com.example.recipesapp.ui.fragment.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.recipesapp.R
import com.example.recipesapp.databinding.FragmentDetailBinding
import com.squareup.picasso.Picasso


class DetailFragment : Fragment() {

    lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        val food = arguments?.getParcelable<FoodModel>("Food")
        binding.tvDetailTitle.text = food!!.name
        Picasso.with(activity).load(food.imgUrl).into(binding.imgDetail)
        var ingredients = ""
        var recipe = ""

        for (a in food.ingredients!!)
            ingredients = "${ingredients}$a\n"
        binding.tvIngredients.text = ingredients

        for (a in food.recipe!!)
            recipe = "${recipe}$a\n\n"
        binding.tvMethod.text = recipe

        return binding.root
    }

}