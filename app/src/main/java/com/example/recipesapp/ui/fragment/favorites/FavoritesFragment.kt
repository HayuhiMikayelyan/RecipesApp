package com.example.recipesapp.ui.fragment.favorites

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.R
import com.example.recipesapp.databinding.FragmentFavoritesBinding
import com.example.recipesapp.ui.fragment.home.DetailFragment
import com.example.recipesapp.ui.fragment.home.FoodModel
import com.example.recipesapp.ui.fragment.home.RecAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FavoritesFragment : Fragment() {
private lateinit var adapter: RecAdapter
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var database: DatabaseReference
    private lateinit var foodList: MutableList<FoodModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater,container,false)

        binding.recFav.layoutManager = LinearLayoutManager(activity)
        foodList = mutableListOf()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFoodList()
    }

    private fun getFoodList() {
        binding.favProgress.visibility = ProgressBar.VISIBLE
        adapter = RecAdapter(foodList)
        binding.recFav.adapter = adapter
        database = FirebaseDatabase.getInstance().getReference("Drinks")
        val favourites = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.email.toString().replace(".", ""))
            .child("favoriteList")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    for (foodSnapshot in snapshot.children) {

                        favourites.addValueEventListener(object : ValueEventListener {
                            @SuppressLint("NotifyDataSetChanged")
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (snap in snapshot.children) {
                                    val food = foodSnapshot.getValue(FoodModel::class.java)
                                    if(snap.getValue(String::class.java)!!.toString() == food!!.name.toString()){

                                        foodList.add(food)
                                        adapter.notifyDataSetChanged()
                                    }
                                }
                            }override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(activity, error.message, Toast.LENGTH_LONG).show()
                            }

                        })
                    }


                    binding.favProgress.visibility = ProgressBar.GONE
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