package com.example.recipesapp.ui.fragment.favorites

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.example.recipesapp.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.databinding.FragmentFavoritesBinding
import com.example.recipesapp.ui.fragment.home.main.DetailFragment
import com.example.recipesapp.ui.models.CategoriesModel
import com.example.recipesapp.ui.models.FoodModel
import com.example.recipesapp.ui.recycler_adapters.FoodRecAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FavoritesFragment : Fragment() {
private lateinit var adapter: FoodRecAdapter
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var dbCategories: DatabaseReference
    private lateinit var foodList: MutableList<FoodModel>
    private lateinit var favList: MutableList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater,container,false)

        binding.recFav.layoutManager = LinearLayoutManager(activity)
        foodList = mutableListOf()
        favList = mutableListOf()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFoodList()
    }

    private fun getFoodList() {
        binding.favProgress.visibility = ProgressBar.VISIBLE
        adapter = FoodRecAdapter(foodList)
        binding.recFav.adapter = adapter
        dbCategories = FirebaseDatabase.getInstance().getReference("Categories")
        val favourites = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.email.toString().replace(".", ""))
            .child("favoriteList")

        favourites.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (fav in snapshot.children){
                    favList.add(fav.getValue(String::class.java)!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }

        })
        dbCategories.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(categories in snapshot.children){
                    val category = categories.getValue(CategoriesModel::class.java)
                    val dbFood = FirebaseDatabase.getInstance().getReference(category!!.name!!)
                    dbFood.addValueEventListener(object : ValueEventListener{
                        @SuppressLint("NotifyDataSetChanged")
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for(foods in snapshot.children){
                                val food = foods.getValue(FoodModel::class.java)
                                if(food!!.name in favList){
                                    foodList.add(food)
                                    adapter.notifyDataSetChanged()
                                }
                            }

                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                        }

                    })
                }
                binding.favProgress.visibility = ProgressBar.GONE
                adapter.onItemClick = {

                    val bundle = Bundle()
                    val fragment = DetailFragment()

                    bundle.putParcelable("Food", it)
                    fragment.arguments = bundle

                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

}