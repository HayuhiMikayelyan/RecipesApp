package com.example.recipesapp.ui.fragment.home.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipesapp.R
import com.example.recipesapp.databinding.FragmentCategoryBinding
import com.example.recipesapp.ui.fragment.home.main.DetailFragment
import com.example.recipesapp.ui.fragment.home.main.HomeFragment
import com.example.recipesapp.ui.models.CategoriesModel
import com.example.recipesapp.ui.recycler_adapters.CategoriesRecAdapter
import com.google.firebase.database.*

class CategoryFragment : Fragment() {

    private lateinit var binding: FragmentCategoryBinding
    private lateinit var categoryList: MutableList<CategoriesModel>
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)

        binding.categoryRec.layoutManager = GridLayoutManager(activity, 2)
        categoryList = mutableListOf()
        getCategories()

        return binding.root
    }

    private fun getCategories() {

        database = FirebaseDatabase.getInstance().getReference("Categories")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    for (categorySnapshot in snapshot.children) {
                        val category = categorySnapshot.getValue(CategoriesModel::class.java)
                        categoryList.add(category!!)
                    }
                    val adapter = CategoriesRecAdapter(categoryList)
                    binding.categoryRec.adapter = adapter
                    binding.progress.visibility = ProgressBar.GONE

                    adapter.onItemClick = {
                        val bundle = Bundle()
                        val fragment = HomeFragment()

                        bundle.putParcelable("Category", it)

                        fragment.arguments = bundle

                        fragmentManager!!.beginTransaction()
                            .replace(R.id.container, fragment)
                            .addToBackStack(null)
                            .commit()
                    }
                }

            }


            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

}