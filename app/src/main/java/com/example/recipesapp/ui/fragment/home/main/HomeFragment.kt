package com.example.recipesapp.ui.fragment.home.main

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.R
import com.example.recipesapp.databinding.FragmentHomeBinding
import com.example.recipesapp.ui.models.CategoriesModel
import com.example.recipesapp.ui.models.FoodModel
import com.example.recipesapp.ui.recycler_adapters.FoodRecAdapter
import com.google.firebase.database.*


class HomeFragment : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var foodList: MutableList<FoodModel>
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.rec.layoutManager = LinearLayoutManager(activity)
        foodList = mutableListOf()

        val category = arguments?.getParcelable<CategoriesModel>("Category")!!
        binding.fragHome.setBackgroundColor(Color.parseColor(category.background.toString()))
        binding.tvCategoryName.text = category.name
        getFoodList(category.name!!)

        return binding.root
    }


    private fun getFoodList(path:String) {

        database = FirebaseDatabase.getInstance().getReference(path)
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    for (foodSnapshot in snapshot.children) {
                        val food = foodSnapshot.getValue(FoodModel::class.java)
                        foodList.add(food!!)
                    }
                    val adapter = FoodRecAdapter(foodList)
                    binding.rec.adapter = adapter
                    binding.progress.visibility = ProgressBar.GONE
                    adapter.onItemClick = {

                        val bundle = Bundle()
                        val fragment = DetailFragment()

                        bundle.putParcelable("Food", it)
                        fragment.arguments = bundle

                        fragmentManager!!.beginTransaction()
                            .replace(R.id.container, fragment)
                            .addToBackStack(null)
                            .commit()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, error.message, Toast.LENGTH_LONG).show()
            }

        })
    }

}