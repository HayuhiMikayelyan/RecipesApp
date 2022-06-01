package com.example.recipesapp.ui.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.google.firebase.database.*
import java.lang.Exception


class HomeFragment : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var foodList: MutableList<FoodModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rec)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        foodList = mutableListOf()
        getFoodList()
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
                    recyclerView.adapter = adapter
                    adapter.onItemClick = {

                        val bundle = Bundle()
                        val fragment = DetailFragment()

                        bundle.putParcelable("Food",it)
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