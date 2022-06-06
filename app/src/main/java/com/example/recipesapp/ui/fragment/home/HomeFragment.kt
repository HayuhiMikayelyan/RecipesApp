package com.example.recipesapp.ui.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.databinding.FragmentHomeBinding
import com.google.firebase.database.*
import java.lang.Exception


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

        getFoodList()

        return binding.root
    }


    private fun getFoodList() {

        database = FirebaseDatabase.getInstance().getReference("Drinks")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    for (foodSnapshot in snapshot.children) {
                        val food = foodSnapshot.getValue(FoodModel::class.java)
                        foodList.add(food!!)
                    }
                    val adapter = RecAdapter(foodList)
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